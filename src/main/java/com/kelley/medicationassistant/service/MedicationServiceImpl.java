package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.feignclient.OpenAiClient;
import com.kelley.medicationassistant.feignclient.RxNormClient;
import com.kelley.medicationassistant.model.Medication;
import com.kelley.medicationassistant.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of MedicationService interface.
 */
@Service
public class MedicationServiceImpl implements MedicationService {

    private final RxNormClient rxNormClient;
    private final OpenAiClient openAiClient;
    private final SearchHistoryService searchHistoryService;

    private final Logger medicationServicelogger = LoggerFactory.getLogger(MedicationServiceImpl.class);

    @Value("${spring.ai.openai.model}")
    private String model;

    public MedicationServiceImpl(RxNormClient rxNormClient, OpenAiClient openAiClient,
                                 SearchHistoryService searchHistoryService) {
        this.rxNormClient = rxNormClient;
        this.openAiClient = openAiClient;
        this.searchHistoryService = searchHistoryService;
    }

    @Override
    public Page<Medication> search(String query, Pageable pageable) {
        // Save search query to database in 'search_history' table
        searchHistoryService.addQueryToSearchHistory(query);

        // Call RxNorm API using OpenFeign
        medicationServicelogger.info("Sending 'getDrugs' request to RxNorm API");
        RxNormGetDrugsResponse response = rxNormClient.getDrugs(query);

        if (response == null || response.getDrugGroup() == null) {
            throw new APIException("Search returned no results");
        }

        List<RxNormGetDrugsResponse.ConceptGroup> conceptGroupList = response.getDrugGroup().getConceptGroup();

        if (conceptGroupList == null) {
            throw new APIException("Search returned no results");
        }

        List<Medication> allMedications = conceptGroupList.stream()
                .filter(conceptGroup -> conceptGroup.getConceptProperties() != null)
                .flatMap(conceptGroup -> conceptGroup.getConceptProperties().stream())
                .map(conceptProperty -> new Medication(
                        conceptProperty.getName(),
                        conceptProperty.getRxcui()
                )).toList();

        // Total number of medications fetched from RxNorm
        int total = allMedications.size();


        /*
          Calculate start and end index based on page and size
        */

        // page 1, size 10 => offset 10
        int start = (int) pageable.getOffset();

        int end = Math.min(start + (pageable.getPageSize()), total);

        // If requested start point is greater than total medication count, return empty page
        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }

        // Construct a sublist of items for the requested page
        List<Medication> medications = allMedications.subList(start, end);

        return new PageImpl<>(medications, pageable, total);


    }

    @Override
    public PromptResponse getInformation(PromptRequest request) {

        // Construct the prompt using helper method and convert to ChatMessage object
        String prompt = constructPromptFromPromptRequest(request);
        ChatMessage message = new ChatMessage("user", prompt);

        // Wrap message into an OpenAiRequest object
        OpenAiRequest openAiRequest = new OpenAiRequest();
        openAiRequest.setModel(model);
        openAiRequest.setMessages(List.of(message));

        // Call OpenAI API using OpenFeign client
        medicationServicelogger.info("Sending chat request to OpenAI API");
        OpenAiResponse openAiResponse = openAiClient.chat(openAiRequest);

        // Create Prompt Response containing OpenAI assistant's reply
        return new PromptResponse(openAiResponse.getChoices()
                .get(0)
                .getMessage()
                .getContent());
    }


    @Override
    public Page<Medication> getRelated(String rxcui, Pageable pageable) {
        // Call RxNorm API using OpenFeign to find related Medications given an rxcui
        medicationServicelogger.info("Sending 'getRelatedDrugs' request to RxNorm API");
        RxNormGetRelatedDrugsResponse response = rxNormClient.getRelatedDrugs(rxcui);

        // If response is null or empty, throw API Exception
        if (response == null || response.getAllRelatedGroup() == null)
            throw new APIException("Search returned no results");

        // Get all concept groups
        List<RxNormGetRelatedDrugsResponse.ConceptGroup> conceptGroupList =
                response.getAllRelatedGroup().getConceptGroup();

        // Check if concept group list is null before converting to stream to avoid errors
        if (conceptGroupList == null)
            throw new APIException("Search returned no results");

        // Extract name and rxcui of all Medications from nested RxNorm API response
        List<Medication> allMedicationList =
                conceptGroupList.stream()
                        .filter(conceptGroup -> conceptGroup.getConceptProperties() != null)
                        .flatMap(conceptGroup -> conceptGroup.getConceptProperties().stream())
                        .map(conceptProperty -> new Medication(
                                conceptProperty.getName(),
                                conceptProperty.getRxcui()
                        ))
                        .toList();


        // Calculate and implement pagination details (RxNorm API does not allow pagination)

        int total = allMedicationList.size();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        // If requested start point is greater than total medication count, return empty page
        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), pageable, end);
        }

        List<Medication> paginatedMedicationList = allMedicationList.subList(start, end);

        return new PageImpl<>(paginatedMedicationList, pageable, total);
    }

    /**
     * Helper method to construct a prompt for OpenAI given the details provided in the {@link PromptRequest} DTO
     *
     * @param request request details
     * @return concatenated prompt string
     */
    private String constructPromptFromPromptRequest(PromptRequest request) {
        return request.getMedicationName() + " " + request.getMedicationChatOption().getValue();
    }


}

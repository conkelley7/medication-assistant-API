package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.feignclient.OpenAiClient;
import com.kelley.medicationassistant.feignclient.RxNormClient;
import com.kelley.medicationassistant.model.Medication;
import com.kelley.medicationassistant.payload.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    private final RxNormClient rxNormClient;
    private final OpenAiClient openAiClient;

    @Value("${spring.ai.openai.model}")
    private String model;

    public MedicationServiceImpl(RxNormClient rxNormClient, OpenAiClient openAiClient) {
        this.rxNormClient = rxNormClient;
        this.openAiClient = openAiClient;
    }

    @Override
    public Page<Medication> search(String query, Pageable pageable) {
        // Call RxNorm API using OpenFeign
        RxNormResponse response = rxNormClient.getDrugs(query);

        /*
        Initialize list to contain all returned medications.
        RxNorm API does not allow for pagination, so custom pagination
        will be implemented below.
         */
        List<Medication> allMedications = new ArrayList<>();

        // Extract desired data from the nested RxNorm API Response
        if (response != null && response.getDrugGroup() != null) {
            List<RxNormResponse.ConceptGroup> conceptGroups = response.getDrugGroup().getConceptGroup();

            if (conceptGroups == null)
                throw new APIException("Search returned no results. Please try a different query.");

            for (RxNormResponse.ConceptGroup group : conceptGroups) {
                if (group.getConceptProperties() != null) {
                    for (RxNormResponse.ConceptProperty prop : group.getConceptProperties()) {
                        allMedications.add(new Medication(
                                prop.getName(),
                                prop.getRxcui()
                        ));
                    }
                }
            }

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

        } else {
            throw new APIException("Search returned no results. Please try a different query.");
        }


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
        OpenAiResponse openAiResponse = openAiClient.chat(openAiRequest);

        // Create Prompt Response containing OpenAI assistant's reply
        return new PromptResponse(openAiResponse.getChoices()
                .get(0)
                .getMessage()
                .getContent());
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

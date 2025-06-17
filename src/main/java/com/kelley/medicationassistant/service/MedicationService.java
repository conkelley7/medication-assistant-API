package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.model.Medication;
import com.kelley.medicationassistant.payload.PromptRequest;
import com.kelley.medicationassistant.payload.PromptResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * MedicationService interface defines contract for service implementations
 */
public interface MedicationService {
    /**
     * Retrieves a Page of {@link Medication} objects given a search query and pagination details
     *
     * @param query medication search query
     * @param pageable pagination details
     * @return Page containing paginated Medication search results
     */
    Page<Medication> search(String query, Pageable pageable);

    /**
     * Retrieve a response from an AI chatbot given a medication name and the type of information desired (general
     * information, dosage, potential side effects)
     *
     * @param request {@link PromptRequest} DTO containing details for the information requests
     * @return {@link PromptResponse} DTO containing message from AI provider
     */
    PromptResponse getInformation(PromptRequest request);

    /**
     * Given an rxcui of a drug (obtained from 'search' method), this method will return a paginated
     * list of {@link Medication} objects representing related drugs
     *
     * @param rxcui rxcui of the drug from which to find related drugs
     * @param pageable pagination details
     * @return Page containing paginated related Medication search results
     */
    Page<Medication> getRelated(String rxcui, Pageable pageable);
}

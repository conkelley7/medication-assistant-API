package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.feignclient.OpenAiClient;
import com.kelley.medicationassistant.feignclient.RxNormClient;
import com.kelley.medicationassistant.model.Medication;
import com.kelley.medicationassistant.model.MedicationChatOption;
import com.kelley.medicationassistant.payload.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicationServiceTest {

    @InjectMocks
    private MedicationServiceImpl medicationService;

    @Mock
    private OpenAiClient openAiClient;

    @Mock
    private RxNormClient rxNormClient;


    @Test
    void testGetInformation_returnsInformation() {
        /*
         * Given
         */

        // Set up a prompt request to pass to 'getInformation' method
        PromptRequest request = new PromptRequest();
        request.setMedicationName("Ibuprofen");
        request.setMedicationChatOption(MedicationChatOption.SIDE_EFFECTS);

        // Define a mock response from OpenAI client
        ChatMessage assistantMessage = new ChatMessage("assistant", "May cause nausea");
        OpenAiResponse mockResponse = new OpenAiResponse(List.of(new OpenAiResponse.Choice(0, assistantMessage)));

        // State that mocked openAI client will always return the mocked response
        when(openAiClient.chat(any(OpenAiRequest.class))).thenReturn(mockResponse);

        /*
         * When
         */

        // Call the method using defined prompt request
        PromptResponse result = medicationService.getInformation(request);

        /*
         * Then
         */

        // Assert the result is not null
        assertNotNull(result);
        // Assert that the resulting PromptResponseDTO 'message' matches mocked response message
        assertEquals("May cause nausea", result.getMessage());
    }

    @Test
    void testSearch_returnsMedications() {
        /*
        * Given
         */

        // Define RxNorm getDrugs mock response
        RxNormGetDrugsResponse.ConceptProperty conceptProperty =
                new RxNormGetDrugsResponse.ConceptProperty("R123", "Ibuprofen");
        RxNormGetDrugsResponse.ConceptGroup conceptGroup =
                new RxNormGetDrugsResponse.ConceptGroup(List.of(conceptProperty));
        RxNormGetDrugsResponse.DrugGroup drugGroup =
                new RxNormGetDrugsResponse.DrugGroup(List.of(conceptGroup));
        RxNormGetDrugsResponse mockResponse = new RxNormGetDrugsResponse(drugGroup);

        // State that mocked RxNorm Client will always return the mock response
        when(rxNormClient.getDrugs("Ibuprofen")).thenReturn(mockResponse);

        /*
        * When
         */

        // Call the method under test
        Page<Medication> result =
                medicationService.search("Ibuprofen", PageRequest.of(0, 10));

        /*
        * Then
         */

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Ibuprofen", result.getContent().get(0).getName());
        assertEquals("R123", result.getContent().get(0).getRxcui());
    }

    @Test
    public void testGetRelated_returnsMedications() {
        /*
        * Given
         */

        // Define RxNorm getRelated Mock response
        RxNormGetRelatedDrugsResponse.ConceptProperty conceptProperty =
                new RxNormGetRelatedDrugsResponse.ConceptProperty(
                        "R123",
                        "Ibuprofen",
                        "synonym",
                        "tty",
                        "EN",
                        "suppress",
                        "umlscui"
                );
        RxNormGetRelatedDrugsResponse.ConceptGroup conceptGroup =
                new RxNormGetRelatedDrugsResponse.ConceptGroup("tty", List.of(conceptProperty));
        RxNormGetRelatedDrugsResponse.AllRelatedGroup allRelatedGroup =
                new RxNormGetRelatedDrugsResponse.AllRelatedGroup("R123", List.of(conceptGroup));
        RxNormGetRelatedDrugsResponse mockResponse = new RxNormGetRelatedDrugsResponse(allRelatedGroup);

        // Define 'getRelated' will always return the mocked response
        when(rxNormClient.getRelatedDrugs("R123")).thenReturn(mockResponse);

        /*
        * When
         */

        // Call the method under test
        Page<Medication> result = medicationService.getRelated("R123", PageRequest.of(0, 10));

        /*
        * Then
         */

        // Make assertions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("R123", result.getContent().get(0).getRxcui());
        assertEquals("Ibuprofen", result.getContent().get(0).getName());
    }
}

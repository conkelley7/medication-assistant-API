package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.feignclient.OpenAiClient;
import com.kelley.medicationassistant.model.MedicationChatOption;
import com.kelley.medicationassistant.payload.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @Test
    void testGetInformationReturnsMockedResponse() {
        // Given
        PromptRequest request = new PromptRequest();
        request.setMedicationName("Ibuprofen");
        request.setMedicationChatOption(MedicationChatOption.SIDE_EFFECTS);

        ChatMessage assistantMessage = new ChatMessage("assistant", "May cause nausea");
        OpenAiResponse mockResponse = new OpenAiResponse(List.of(new OpenAiResponse.Choice(0, assistantMessage)));

        when(openAiClient.chat(any(OpenAiRequest.class))).thenReturn(mockResponse);

        // When
        PromptResponse result = medicationService.getInformation(request);

        // Then
        assertNotNull(result);
        assertEquals("May cause nausea", result.getMessage());
    }
}

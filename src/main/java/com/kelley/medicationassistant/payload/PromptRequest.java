package com.kelley.medicationassistant.payload;

import com.kelley.medicationassistant.model.MedicationChatOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * DTO for information requests.
 *
 * Each information request is to contain a name of a medication,
 * as well as one of the approved {@link MedicationChatOption}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromptRequest {
    @NotNull
    private String medicationName;
    @NotNull
    private MedicationChatOption medicationChatOption;
}

package com.kelley.medicationassistant.model;

import lombok.Getter;

/**
 * Provides options for what type of information a user would like to retrieve when making an information request.
 * Options include general information, dosage, or potential side effects.
 *
 * Value is appended to the end of the OpenAI prompt, after the medication name
 */
@Getter
public enum MedicationChatOption {
    GENERAL_INFORMATION("general information"),
    DOSAGE("dosage"),
    SIDE_EFFECTS("potential side effects");

    private final String value;

    MedicationChatOption(String value) {
        this.value = value;
    }

}

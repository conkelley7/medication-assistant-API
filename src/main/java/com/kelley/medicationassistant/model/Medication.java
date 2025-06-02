package com.kelley.medicationassistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for Medication objects.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    private String name;
    private String rxcui;
}

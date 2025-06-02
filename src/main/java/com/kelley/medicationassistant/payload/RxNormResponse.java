package com.kelley.medicationassistant.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Follows expected formatting for responses from RxNorm API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RxNormResponse {
    private DrugGroup drugGroup;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrugGroup {
        private List<ConceptGroup> conceptGroup;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptGroup {
        private List<ConceptProperty> conceptProperties;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptProperty {
        private String rxcui;
        private String name;
    }
}

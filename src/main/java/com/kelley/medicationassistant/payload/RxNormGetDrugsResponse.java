package com.kelley.medicationassistant.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Follows expected formatting for 'getDrugs' response from RxNorm API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RxNormGetDrugsResponse {
    private DrugGroup drugGroup;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrugGroup {
        private List<ConceptGroup> conceptGroup;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptGroup {
        private List<ConceptProperty> conceptProperties;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptProperty {
        private String rxcui;
        private String name;
    }
}

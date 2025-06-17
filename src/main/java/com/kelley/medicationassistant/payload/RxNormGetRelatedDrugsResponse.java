package com.kelley.medicationassistant.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Follows expected format for 'getAllRelatedInfo' response from RxNorm API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RxNormGetRelatedDrugsResponse {
    private AllRelatedGroup allRelatedGroup;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllRelatedGroup {
        private String rxcui;
        private List<ConceptGroup> conceptGroup;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptGroup {
        private String tty;
        private List<ConceptProperty> conceptProperties;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptProperty {
        private String rxcui;
        private String name;
        private String synonym;
        private String tty;
        private String language;
        private String suppress;
        private String umlscui;
    }


}

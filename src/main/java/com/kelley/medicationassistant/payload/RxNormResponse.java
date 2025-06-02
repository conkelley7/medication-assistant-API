package com.kelley.medicationassistant.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RxNormResponse {
    private DrugGroup drugGroup;

    @Data
    public static class DrugGroup {
        private List<ConceptGroup> conceptGroup;
    }

    @Data
    public static class ConceptGroup {
        private List<ConceptProperty> conceptProperties;
    }

    @Data
    public static class ConceptProperty {
        private String rxcui;
        private String name;
    }
}

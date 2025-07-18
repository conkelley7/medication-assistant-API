package com.kelley.medicationassistant.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for search-history related requests involving {@link com.kelley.medicationassistant.model.Search}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    private Long searchHistoryId;
    private String searchQuery;
    private LocalDateTime searchedAt;
    private Long userId;
}

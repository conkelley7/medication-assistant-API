package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.model.Search;
import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.payload.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contract for methods relating to search history functionality
 */
public interface SearchHistoryService {

    /**
     * Creates a {@link Search} record in database with a given query
     *
     * @param query the user's query to be saved
     * @return SearchDTO object with saved Search details
     */
    SearchDTO addQueryToSearchHistory(String query);

    /**
     * Retrieves search history for a given user
     *
     * @param user The user for whom to retrieve search history
     * @param pageable pagination parameters
     * @return page of search history DTO objects
     */
    Page<SearchDTO> getSearchHistoryByUser(User user, Pageable pageable);

    /**
     * Retrieves search history for a user given a user ID
     *
     * @param userId The ID of the user for whom to retrieve search history
     * @param pageable pagination parameters
     * @return page of search history DTO objects
     */
    Page<SearchDTO> getSearchHistoryByUserId(Long userId, Pageable pageable);
}

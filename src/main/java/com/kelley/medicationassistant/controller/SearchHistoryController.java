package com.kelley.medicationassistant.controller;

import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.dto.SearchDTO;
import com.kelley.medicationassistant.security.service.AuthUtil;
import com.kelley.medicationassistant.service.SearchHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller class, exposes API endpoints relating to user search history
 */
@RestController
@RequestMapping( "/api/v1/search-history" )
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;
    private final AuthUtil authUtil;

    public SearchHistoryController( SearchHistoryService searchHistoryService, AuthUtil authUtil ) {
        this.searchHistoryService = searchHistoryService;
        this.authUtil = authUtil;
    }

    /**
     * Retrieve search history for the currently logged-in user
     *
     * @param pageable pagination parameters
     * @return page of search history DTO objects
     */
    @GetMapping
    public ResponseEntity<Page<SearchDTO>> getSearchHistoryForLoggedInUser( Pageable pageable ) {

        User user = authUtil.getLoggedInUser( );
        // TODO match controller method name and service method name
        Page<SearchDTO> searchDTOPage = searchHistoryService.getSearchHistoryByUser( user, pageable );

        return new ResponseEntity<>( searchDTOPage, HttpStatus.OK );

    }

}

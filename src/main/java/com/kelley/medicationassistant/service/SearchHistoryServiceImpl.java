package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.model.Search;
import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.dto.SearchDTO;
import com.kelley.medicationassistant.repository.SearchHistoryRepository;
import com.kelley.medicationassistant.repository.UserRepository;
import com.kelley.medicationassistant.security.service.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation of SearchHistoryService interface
 */
@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    public SearchHistoryServiceImpl( SearchHistoryRepository searchHistoryRepository, AuthUtil authUtil, ModelMapper modelMapper ) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public SearchDTO addQueryToSearchHistory( String query ) {

        Search search = new Search( );
        search.setUser( authUtil.getLoggedInUser( ) );
        search.setSearchQuery( query );

        Search savedSearch = searchHistoryRepository.save( search );

        return modelMapper.map( savedSearch, SearchDTO.class );

    }

    @Override
    public Page<SearchDTO> getSearchHistoryByUser( User user, Pageable pageable ) {

        Page<Search> searchHistoryPage = searchHistoryRepository.findAllByUser( user, pageable );

        return searchHistoryPage.map( entity -> modelMapper.map( entity, SearchDTO.class ) );
    }
}

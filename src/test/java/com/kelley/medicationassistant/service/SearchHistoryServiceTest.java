package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.model.Search;
import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.payload.SearchDTO;
import com.kelley.medicationassistant.repository.SearchHistoryRepository;
import com.kelley.medicationassistant.repository.UserRepository;
import com.kelley.medicationassistant.security.service.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchHistoryServiceTest {

    @InjectMocks
    private SearchHistoryServiceImpl searchHistoryService;

    @Mock
    private SearchHistoryRepository searchHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private ModelMapper modelMapper;

    private User user;
    private Search search1;
    private Search search2;
    private SearchDTO searchDTO1;
    private SearchDTO searchDTO2;


    @BeforeEach
    void setup() {
        user = new User(1L, "name", "password", "email", Collections.emptyList());

        search1 = new Search();

        search2 = new Search();

        searchDTO1 = new SearchDTO();

        searchDTO2 = new SearchDTO();
    }

    @Test
    void addQueryToSearchHistory_shouldReturnSearchDTO() {
        // Arrange
        when(authUtil.getLoggedInUser()).thenReturn(user);
        when(searchHistoryRepository.save(any(Search.class))).thenReturn(search1);
        when(modelMapper.map(search1, SearchDTO.class)).thenReturn(searchDTO1);

        // Act
        SearchDTO result = searchHistoryService.addQueryToSearchHistory("Lipitor");

        // Assert
        assertEquals(result, searchDTO1);
    }

    @Test
    void getSearchHistoryByUser_shouldReturnPageOfSearchDTOs() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Search> searchHistoryPage = new PageImpl<>(List.of(search1, search2));
        Page<SearchDTO> searchHistoryDTOPage = new PageImpl<>(List.of(searchDTO1, searchDTO2));

        when(searchHistoryRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(searchHistoryPage);
        when(modelMapper.map(search1, SearchDTO.class)).thenReturn(searchDTO1);
        when(modelMapper.map(search2, SearchDTO.class)).thenReturn(searchDTO2);

        // Act
        Page<SearchDTO> result = searchHistoryService.getSearchHistoryByUser(user, pageRequest);

        // Assert
        assertEquals(result, searchHistoryDTOPage);
        assertEquals(2, result.getContent().size());
        assertEquals(searchDTO1, result.getContent().get(0));
        assertEquals(searchDTO2, result.getContent().get(1));
    }

    @Test
    void getSearchHistoryByUserId_shouldReturnPageOfSearchDTOs() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Search> searchHistoryPage = new PageImpl<>(List.of(search1));
        Page<SearchDTO> searchHistoryDTOPage = new PageImpl<>(List.of(searchDTO1));

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(searchHistoryRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(searchHistoryPage);
        when(modelMapper.map(search1, SearchDTO.class)).thenReturn(searchDTO1);

        // Act
        Page<SearchDTO> result = searchHistoryService.getSearchHistoryByUserId(user.getUserId(), pageRequest);

        // Assert
        assertEquals(searchHistoryDTOPage, result);
        assertEquals(1, result.getContent().size());
        assertEquals(searchDTO1, result.getContent().get(0));
    }

    @Test
    void getSearchHistoryByUserId_shouldThrowAPIException() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Long nonExistentId = -1L;

        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act and Assert
        APIException exception = assertThrows(APIException.class, () -> {
            searchHistoryService.getSearchHistoryByUserId(nonExistentId, pageRequest);
        });

        // Assert exception message is correct
        assertEquals("User not found with ID: " + nonExistentId, exception.getMessage());
    }

}

package com.kelley.medicationassistant.repository;

import com.kelley.medicationassistant.model.Search;
import com.kelley.medicationassistant.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Defines methods for interacting with 'search_history' table in DB (via 'Search' entity)
 */
@Repository
public interface SearchHistoryRepository extends JpaRepository<Search, Long> {
    Page<Search> findAllByUser(User user, Pageable pageable);
}

package com.kelley.medicationassistant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 'Search' entity, linked to 'search_history' table in database
 */
@Entity
@Table(name = "search_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_history_id")
    private Long searchHistoryId;

    @Column(name = "search_query")
    private String searchQuery;

    @Column(name = "searched_at")
    @CreationTimestamp
    private LocalDateTime searchedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

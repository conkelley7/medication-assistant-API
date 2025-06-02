package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.model.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicationService {
    Page<Medication> search(String query, Pageable pageable);
}

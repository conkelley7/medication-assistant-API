package com.kelley.medicationassistant.controller;

import com.kelley.medicationassistant.model.Medication;
import com.kelley.medicationassistant.service.MedicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medication")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Medication>> search(@RequestParam String query,
                                                   Pageable pageable) {
        Page<Medication> response = medicationService.search(query, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

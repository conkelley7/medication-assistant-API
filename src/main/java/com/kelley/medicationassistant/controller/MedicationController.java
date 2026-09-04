package com.kelley.medicationassistant.controller;

import com.kelley.medicationassistant.model.Medication;
import com.kelley.medicationassistant.dto.PromptRequest;
import com.kelley.medicationassistant.dto.PromptResponse;
import com.kelley.medicationassistant.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller class, exposes API endpoints relating to medication searches and chat queries.
 */
@RestController
@RequestMapping( "/api/v1/medication" )
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController( MedicationService medicationService ) {
        this.medicationService = medicationService;
    }

    /**
     * Exposes an endpoint for looking up medications using RxNorm API.
     *
     * @param query Medication search query, passed to RxNorm.
     * @param pageable allows for pagination variables
     * @return ResponseEntity with page of medications, and 200 OK status.
     */
    @GetMapping( "/search" )
    public ResponseEntity<Page<Medication>> search( @RequestParam String query, Pageable pageable ) {

        Page< Medication > response = medicationService.search( query, pageable );
        return new ResponseEntity<>( response, HttpStatus.OK );

    }

    /**
     * Exposes endpoint for looking up related medications from a Medication's rxcui code
     *
     * @param rxcui for which you would like to look up related medications
     * @param pageable allows for pagination variables
     * @return ResponseEntity with page of medications, and 200 OK status
     */
    @GetMapping( "/related/{rxcui}" )
    public ResponseEntity<Page<Medication>> getRelated( @PathVariable String rxcui, Pageable pageable ) {

        Page<Medication> response = medicationService.getRelated( rxcui, pageable );
        return new ResponseEntity<>( response, HttpStatus.OK );

    }

    /**
     * Exposes an endpoint for retrieving information from OpenAI regarding a particular medication, and an
     * information type selection. See {@link com.kelley.medicationassistant.model.MedicationChatOption}
     *
     * @param request {@link PromptRequest} DTO containing medication name, and desired information option
     * @return ResponseEntity containing the AI model's response to the inquiry from OpenAI and 200 OK status
     */
    @PostMapping( "/information" )
    public ResponseEntity<PromptResponse> getInformation( @Valid @RequestBody PromptRequest request ) {

        PromptResponse response = medicationService.getInformation( request );
        return new ResponseEntity<>( response, HttpStatus.OK );

    }
}

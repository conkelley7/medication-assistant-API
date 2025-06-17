package com.kelley.medicationassistant.feignclient;

import com.kelley.medicationassistant.payload.RxNormGetDrugsResponse;
import com.kelley.medicationassistant.payload.RxNormGetRelatedDrugsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FeignClient for making requests to RxNorm API
 */
@FeignClient(name = "rxNormClient", url = "https://rxnav.nlm.nih.gov/REST")
public interface RxNormClient {

    @GetMapping(value = "/drugs.json")
    RxNormGetDrugsResponse getDrugs(@RequestParam("name") String name);

    @GetMapping(value = "/rxcui/{rxcui}/allrelated.json")
    RxNormGetRelatedDrugsResponse getRelatedDrugs(@PathVariable String rxcui);
}

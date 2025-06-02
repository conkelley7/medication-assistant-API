package com.kelley.medicationassistant.feignclient;

import com.kelley.medicationassistant.payload.RxNormResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FeignClient for making requests to RxNorm API
 */
@FeignClient(name = "rxNormClient", url = "https://rxnav.nlm.nih.gov/REST")
public interface RxNormClient {

    @GetMapping(value = "/drugs.json")
    RxNormResponse getDrugs(@RequestParam("name") String name);
}

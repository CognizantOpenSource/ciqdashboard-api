package com.cognizant.idashboardapi.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface NLPApiClient {
    @GetMapping(value = "/getkeywords")
    List<String> getKeywords(@Valid @RequestParam @NotBlank String searchString);
}

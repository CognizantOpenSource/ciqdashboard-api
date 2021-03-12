package com.cognizant.ciqdashboardapi.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

public interface AuthApiClient {

    @GetMapping(value = "/teams/names/current-user")
    List<String> getTeamNamesByCurrentUser(@RequestHeader Map<String, Object> headers);
}

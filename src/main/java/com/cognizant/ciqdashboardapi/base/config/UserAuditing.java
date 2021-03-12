package com.cognizant.ciqdashboardapi.base.config;

import com.cognizant.ciqdashboardapi.base.models.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditing implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            return Optional.of("");

        Object object = authentication.getPrincipal();
        String username;

        if (object instanceof UserPrincipal)
            username = ((UserPrincipal)object).getEmail();
        else
            username = "";

        return Optional.of(username);
    }
}
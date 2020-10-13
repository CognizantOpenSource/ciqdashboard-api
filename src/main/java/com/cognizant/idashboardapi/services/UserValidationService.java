package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.base.models.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    @Value("${app.leap.permission.admin}")
    private String adminPermission;

    public boolean isAdmin(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(adminPermission);
        return userPrincipal.getAuthorities().contains(adminAuthority);
    }
}

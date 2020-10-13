package com.cognizant.idashboardapi.base.config;

import com.cognizant.idashboardapi.base.models.UserPrincipal;
import com.cognizant.idashboardapi.base.security.CustomPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.function.Predicate;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;

    @Value("${app.leap.permission.admin:leap.permission.admin}")
    private String adminPermission;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }

    @Bean("adminValidator")
    public Predicate<UserPrincipal> getAdminValidator() {
        return userPrincipal -> Objects.nonNull(userPrincipal.getAuthorities()) && userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(adminPermission::equalsIgnoreCase);
    }

}
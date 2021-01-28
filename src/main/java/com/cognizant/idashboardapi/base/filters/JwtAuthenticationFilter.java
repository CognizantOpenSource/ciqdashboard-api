package com.cognizant.idashboardapi.base.filters;

import com.cognizant.idashboardapi.base.models.User;
import com.cognizant.idashboardapi.base.models.UserPrincipal;
import com.cognizant.idashboardapi.base.services.CustomUserDetailsService;
import com.cognizant.idashboardapi.base.services.JwtTokenService;
import com.cognizant.idashboardapi.base.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService tokenProvider;

    //@Autowired
    //private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            log.debug("jwt token ****** " + jwt);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                User user = tokenProvider.getUser(jwt);
                List<String> permissions = tokenProvider.getPermissions(jwt);
                UserDetails userDetails = UserPrincipal.create(user, permissions);
                if (userDetails != null && userDetails.isEnabled()) {
                    log.debug("'{}' logged in", userDetails.getUsername());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new UsernameNotFoundException("Inactive or Username not found");
                }
            }
        } catch (Exception ex) {
            log.error("Could not authenticate user", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        //String uuidToken = "";
        if (StringUtils.hasText(bearerToken)) {
            //uuidToken = TokenUtil.getUUIDStringFromToken(bearerToken);
            //if (StringUtils.hasText(uuidToken)) {
                //String tokenFromDB = ''; //tokenProvider.getTokenFromDB(uuidToken);
                //if (StringUtils.hasText(tokenFromDB)) return tokenFromDB;
            //}
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            tokenProvider.validateUserSession(token);
            return token;
        } else {
            throw new ResourceAccessException("Invalid Auth token - " + bearerToken);
        }
    }
}
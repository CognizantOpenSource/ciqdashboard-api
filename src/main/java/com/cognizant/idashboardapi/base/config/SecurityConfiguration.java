package com.cognizant.idashboardapi.base.config;

import com.cognizant.idashboardapi.base.filters.JwtAuthenticationFilter;
import com.cognizant.idashboardapi.base.security.JwtAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${app.cors.origin:''}")
    private String allowedOrigin;

    @Value("${app.cors.enabled:false}")
    private boolean corsEnabled;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public FilterRegistrationBean processCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (corsEnabled) {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.applyPermitDefaultValues();
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigin));
            configuration.addAllowedMethod(HttpMethod.PUT);
            configuration.addAllowedMethod(HttpMethod.DELETE);
            configuration.setAllowCredentials(true);
            source.registerCorsConfiguration("/**", configuration);
        }
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    private HttpSecurity cors(HttpSecurity http) throws Exception {
        if (corsEnabled) {
            return http.cors().and();
        } else {
            return http;
        }
    }

    private void permitMatch(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry, String text) {
        registry.antMatchers("/" + text + "/**",
                "/api/" + text + "/**",
                "/api/v{\\d+}/" + text + "/**")
                .permitAll();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("configure(HttpSecurity): Processing");
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = cors(http)
                /**
                 * disable csrf as application uses jwt token for auth not sessions
                 */
                .csrf().disable()
                .authorizeRequests();
        permitMatch(registry, "auth/token");
        permitMatch(registry, "auth/signup");
        permitMatch(registry, "v2/api-docs");
        permitMatch(registry, "v3/api-docs");
        permitMatch(registry, "docs");
        permitMatch(registry, "swagger-resources");
        permitMatch(registry, "swagger-ui");
        permitMatch(registry, "swagger-ui.html");
        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        registry.anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall fireWall = new DefaultHttpFirewall();
        fireWall.setAllowUrlEncodedSlash(true);
        return fireWall;
    }
}

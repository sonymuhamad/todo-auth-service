package com.sony.authservice.config;

import com.sony.authservice.filter.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserFilter adminUserIdFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                )
                .addFilterBefore(adminUserIdFilter, SecurityContextPersistenceFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable())   // ✅ Disable Basic Auth
                .formLogin(formLogin -> formLogin.disable()); // ✅

        return http.build();
    }
}


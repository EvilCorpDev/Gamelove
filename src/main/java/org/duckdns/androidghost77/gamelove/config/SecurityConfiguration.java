package org.duckdns.androidghost77.gamelove.config;

import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.UserRoleRepository;
import org.duckdns.androidghost77.gamelove.security.DbUserDetailsManager;
import org.duckdns.androidghost77.gamelove.security.JwtAuthenticationEntryPoint;
import org.duckdns.androidghost77.gamelove.security.JwtTokenProvider;
import org.duckdns.androidghost77.gamelove.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DbUserDetailsManager userDetailsManager(
            UserRepository userRepo, UserRoleRepository userRoleRepository, BCryptPasswordEncoder passwordEncoder) {
        return new DbUserDetailsManager(userRepo, userRoleRepository, passwordEncoder);
    }

    @Bean
    public JwtTokenProvider tokenProvider(@Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expirationInMs}") int jwtExpirationInMs) {
        return new JwtTokenProvider(jwtSecret, jwtExpirationInMs);
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider tokenProvider,
            DbUserDetailsManager userDetailsManager) {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsManager);
    }
}

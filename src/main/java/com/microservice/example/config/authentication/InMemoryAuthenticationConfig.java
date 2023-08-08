package com.microservice.example.config.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@Profile("IN-MEMORY")
public class InMemoryAuthenticationConfig {

    // In-memory user details manager for creating test users
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder().passwordEncoder(input -> passwordEncoder().encode(input))
                .username("user")
                .password("user-password")
                .roles("USER")
                .build();

        UserDetails admin = User.builder().passwordEncoder(input -> passwordEncoder().encode(input))
                .username("admin")
                .password("admin-password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    // Bean for password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // @see BCryptPasswordEncoder | DelegatingPasswordEncoder
        return NoOpPasswordEncoder.getInstance(); // Do NOT use in production!
    }
}

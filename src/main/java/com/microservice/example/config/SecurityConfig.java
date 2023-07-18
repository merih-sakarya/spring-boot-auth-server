package com.microservice.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**", "/assets/**", "/webjars/**", "/login", "/error").permitAll()
                .anyRequest().authenticated());
        http.formLogin(formLogin ->
                formLogin.loginPage("/login") // setting the login page
        );
        return http.build();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true); // allow url encoding (enables usage of '%')
        firewall.setAllowSemicolon(true); // allow usage of ';'
        return firewall;
    }

    // In-memory user details manager for creating test users
    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager() {
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

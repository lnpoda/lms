package com.example.lms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests->requests.requestMatchers("/customer/**").permitAll()
                .requestMatchers("/loan/**").permitAll()
                .requestMatchers("/loanApplication/**").permitAll()
                .requestMatchers("/reports/**").permitAll()
                .requestMatchers("/h2-console/**", "/error").permitAll());
        http.csrf(config->config.disable());
        http.cors(config->config.disable());

        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }
}

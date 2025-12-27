package com.example.lms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests->requests.requestMatchers("/customer/**").hasAnyRole("APPLICANT", "ADMIN")
                .requestMatchers("/loan/**").hasAnyRole("APPLICANT", "ADMIN")
                .requestMatchers("/loanApplication/**").hasRole("ADMIN")
                .requestMatchers("/reports/**").hasRole("ADMIN")
                .requestMatchers("/h2-console/**", "/error").permitAll());
        http.csrf(config->config.disable());
        http.cors(config->config.disable());

        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());
        http.headers(headers->headers.frameOptions(frame->frame.disable()));
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("loanuser").password("$2y$10$l33IvbbF0NskfCwSqBtYqufrwEVADTj6b4YwFdsp9DOHAwcaQeOju").authorities("read").build();
//        UserDetails admin = User.withUsername("loanadmin").password("$2y$10$2zYwAUjNWDEem3/JrL/9ye7n7zw819OuxOShWLWobjHp3nuFy3DpW").authorities("admin").build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

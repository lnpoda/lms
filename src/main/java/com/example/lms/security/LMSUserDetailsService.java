package com.example.lms.security;

import com.example.lms.entity.LMSUser;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.LMSUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class LMSUserDetailsService implements UserDetailsService {

    private final LMSUserRepository LMSUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("loading user by email: {}", username);
        LMSUser LMSUser = LMSUserRepository.findByEmail(username)
                .orElseThrow(()->new ResourceNotFoundException("LMSUser", "email", username));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(LMSUser.getRole().getRole()));

        return new org.springframework.security.core.userdetails.User(username, LMSUser.getPassword(), authorities);
    }
}

package com.gym.authservice.domain;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gym.authservice.infrastructure.persistence.UserJpaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;
    public CustomUserDetailsService(UserJpaRepository userJpaRepository){
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("-------HERE-------");
        var user = userJpaRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")); 
        
        return User.builder()                                                 
            .username( user.getUsername() )                                                            
            .password( user.getPassword() )
            .authorities( "ROLE_" + user.getRole() )
            .build();
    }}

package com.example.blog.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("user")) {
            return User.builder()
                    .username("user")
                    .password("password")
                    .roles("USER")
                    .build();
        }
        throw new UsernameNotFoundException("given username is not found: username = " + username);
    }
}

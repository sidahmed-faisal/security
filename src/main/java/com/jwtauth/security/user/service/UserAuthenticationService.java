package com.jwtauth.security.user.service;

import com.jwtauth.security.user.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface UserAuthenticationService {
    Optional<String> login(String username,String password);
    User findByToken(String token);
//    void logout(UserDetailsImpl userDetails);
}

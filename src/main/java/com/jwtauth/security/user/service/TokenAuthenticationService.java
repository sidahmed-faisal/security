package com.jwtauth.security.user.service;

import com.google.common.collect.ImmutableMap;
import com.jwtauth.security.user.model.User;
import com.jwtauth.security.user.model.UserDetailsImpl;
import com.jwtauth.security.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

public class TokenAuthenticationService implements UserAuthenticationService{
    private final  TokenService tokenService;
    private final UserRepository userRepository;

    @Autowired
    public TokenAuthenticationService(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<String> login(String username, String password) {
        return Optional.ofNullable(userRepository
                .findByUserDetails_Username(username))
                .filter(user -> user.get().getUserDetails().getPassword().equals(password))
                .map(user -> tokenService.expiring(ImmutableMap.of("username",username)));
    }

    @Override
    public User findByToken(String token) {
        Map<String,String> result = tokenService.verify(token);
        return userRepository.findByUserDetails_Username(result.get("username")).get();
    }

//    @Override
//    public void logout(UserDetailsImpl userDetails) {
//
//    }
}

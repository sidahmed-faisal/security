package com.jwtauth.security.user.controller;

import com.jwtauth.security.user.model.User;
import com.jwtauth.security.user.model.UserDetailsImpl;
import com.jwtauth.security.user.repository.UserRepository;
import com.jwtauth.security.user.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public UserController(UserRepository userRepository, UserAuthenticationService userAuthenticationService) {
        this.userRepository = userRepository;
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserDetailsImpl userDetails){
        userRepository.save(new User(userDetails));
        return  login(userDetails);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDetailsImpl userDetails){
        return userAuthenticationService
                .login(userDetails.getUsername(),userDetails.getPassword())
                .orElseThrow(()-> new RuntimeException("invalid login details"));
    }
}

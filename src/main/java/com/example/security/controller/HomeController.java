package com.example.security.controller;

import com.example.security.model.MyUser;
import com.example.security.repository.UserRepository;
import com.example.security.service.JwtService;
import com.example.security.service.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    JwtService jwtService;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public HomeController(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

    @GetMapping("/users")
    public List<MyUser> users() {
        return userRepository.findAll();
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.username(),
                        loginForm.password()
                )
        );

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken((UserDetails) authentication.getPrincipal());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}



package com.example.security.controller;

import com.example.security.model.MyUser;
import com.example.security.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}

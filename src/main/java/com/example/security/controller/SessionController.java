package com.example.security.controller;

import com.example.security.model.LoginForm;
import com.example.security.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.username(),
                        loginForm.password()
                )
        );

        if(authentication.isAuthenticated()) {
            return sessionService.createJwtSession((UserDetails) authentication.getPrincipal());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @GetMapping("/api/logout")
    public String logout(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        sessionService.clearSession(authHeader.substring(7));
        return "Logout successful";
    }
}

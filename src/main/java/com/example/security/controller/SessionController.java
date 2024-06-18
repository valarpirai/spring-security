package com.example.security.controller;

import com.example.security.model.LoginForm;
import com.example.security.model.MyUser;
import com.example.security.model.RegistrationForm;
import com.example.security.service.SessionService;
import com.example.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SessionController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    /**
     * Simple User Registration form
     * @param registrationForm
     * @return
     */
    @PostMapping("/register")
    public MyUser register(@RequestBody RegistrationForm registrationForm) {
        MyUser user = new MyUser();
        user.setUsername(registrationForm.username());
        user.setPassword(bCryptPasswordEncoder.encode(registrationForm.password()));
        user.setRole(registrationForm.role());
        return userService.createUser(user);
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
            return sessionService.createJwtSession((UserDetails) authentication.getPrincipal());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        sessionService.clearSession(authHeader.substring(7));
        return "Logout successful";
    }
}

package com.example.security.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (authenticated()) {
//            response.setStatus(FORBIDDEN.value());
//        }
        filterChain.doFilter(request, response);
    }
//
//    private boolean attemptAuthentication(HttpServletRequest request) {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        if (username == null || password == null) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//
//        String decodedPwd = new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8);
//        logger.info("Username is: {} , Password is: ******", username);
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, decodedPwd);
//        return authenticationManager.authenticate(authRequest);
//    }

//    private boolean authenticated(HttpServletRequest request) {
//        String username = request.getParameter("username");
//        return true;
//    }
}

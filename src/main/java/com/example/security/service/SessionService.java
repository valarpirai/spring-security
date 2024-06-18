package com.example.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    @Autowired
    JwtService jwtService;

    @Autowired
    RedisService redisService;

    public String createJwtSession(UserDetails userDetails) {
        var jwtToken = jwtService.generateToken(userDetails);
        redisService.set(sessionKey(userDetails.getUsername()), jwtToken);
        return jwtToken;
    }

    public String extractUsername(String jwtToken) {
        var username = jwtService.extractUsername(jwtToken);
        var sessionData = redisService.get(sessionKey(username));
        if (sessionData != null) {
            return username;
        } else {
            return null;
        }
    }

    private String sessionKey(String username) {
        return "session:" + username;
    }
}

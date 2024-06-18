package com.example.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SessionService {
    final static long SESSION_TTL = TimeUnit.MINUTES.toSeconds(30);

    @Autowired
    JwtService jwtService;

    @Autowired
    RedisService redisService;

    public String createJwtSession(UserDetails userDetails) {
        var jwtToken = jwtService.generateToken(userDetails);
        redisService.set(sessionKey(userDetails.getUsername()), jwtToken, SESSION_TTL);
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

    public void clearSession(String username) {
        redisService.del(sessionKey(username));
    }
}

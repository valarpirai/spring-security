package com.example.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService {
    final static long SESSION_TTL = TimeUnit.MINUTES.toSeconds(30);

    @Autowired
    JwtService jwtService;

    @Autowired
    RedisService redisService;

    /**
     * Create JWT session token on Login/Authenticate
     * @param userDetails
     * @return
     */
    public String createJwtSession(UserDetails userDetails) {
        var timestampStr = String.valueOf(Instant.now().toEpochMilli());
        var jwtToken = jwtService.generateToken(userDetails, timestampStr);
        redisService.set(sessionKey(userDetails.getUsername(), timestampStr), jwtToken, SESSION_TTL);
        return jwtToken;
    }

    /**
     * Extract Username from JWT token
     * @param jwtToken
     * @return
     */
    public String extractUsername(String jwtToken) {
        var sessionData = jwtService.extractSessionData(jwtToken);
        var sessionToken = redisService.get(sessionKey(sessionData[0], sessionData[1]));
        if (sessionToken != null) {
            return sessionData[0];
        } else {
            return null;
        }
    }

    /**
     * Clear JWT Session token on Logout
     * @param jwtToken
     */
    public void clearSession(String jwtToken) {
        var sessionData = jwtService.extractSessionData(jwtToken);
        redisService.del(sessionKey(sessionData[0], sessionData[1]));
    }

    /**
     * Redis Key for storing JWT session token. Support for multiple JWT sessions
     * @param username - Logged in username
     * @param suffix - Timestamp in milliseconds
     * @return
     */
    private String sessionKey(String username, String suffix) {
        return "session:" + username + ":" + suffix;
    }
}

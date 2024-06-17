package com.example.security;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

public class JwtTokenTest {

    @Test
    public void testGenerateSecretKey() {
        var secretKey = Jwts.SIG.HS512.key().build();
        var encodedKey = DatatypeConverter.printHexBinary(secretKey.getEncoded());
        System.out.println("Encoded Key -> " + encodedKey);
    }


}

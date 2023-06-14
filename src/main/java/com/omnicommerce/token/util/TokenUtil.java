package com.omnicommerce.token.util;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

public class TokenUtil {
    static private final String SECRET_KEY = "SECRET";
    static private final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    static private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    static private final Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    static public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    static public boolean parseJwt(String token) {
        try {
            Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (SignatureException e) {
            return false;
        }
        return true;
    }

    static public String extractTokenFromHeader(String authorizationHeader) {
        String beginningBearChars = "Bear ";
        if (!authorizationHeader.startsWith(beginningBearChars)) {
            throw new IllegalArgumentException();
        }
        return authorizationHeader.substring(beginningBearChars.length());
    }

    static public String getSubject(String token) {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
    }
}

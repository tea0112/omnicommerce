package com.omnicommerce.token.util;

import io.jsonwebtoken.*;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

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

    static public void parseJwt(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
    }

    static public String extractTokenFromHeader(String authorizationHeader) {
        String beginningBearerChars = "Bearer ";
        if (!authorizationHeader.startsWith(beginningBearerChars)) {
            throw new IllegalArgumentException();
        }
        return authorizationHeader.substring(beginningBearerChars.length());
    }

    static public String getSubject(String token) {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
    }
}

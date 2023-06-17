package com.omnicommerce.token.util;

import io.jsonwebtoken.*;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class TokenUtil {
  private static final String SECRET_KEY = "SECRET";
  private static final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
  private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
  private static final Key signingKey =
      new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

  public static String generateToken(String username) {

    return Jwts.builder().setSubject(username).signWith(signatureAlgorithm, signingKey).compact();
  }

  public static void parseJwt(String token)
      throws ExpiredJwtException,
          UnsupportedJwtException,
          MalformedJwtException,
          SignatureException,
          IllegalArgumentException {
    Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
  }

  public static String extractTokenFromHeader(String authorizationHeader) {
    String beginningBearerChars = "Bearer ";
    if (!authorizationHeader.startsWith(beginningBearerChars)) {
      throw new IllegalArgumentException();
    }
    return authorizationHeader.substring(beginningBearerChars.length());
  }

  public static String getSubject(String token) {
    return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
  }
}

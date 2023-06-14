package com.omnicommerce.token.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    @Test
    void extractTokenFromHeaderPass() {
        String bearToken = "Bear eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGFpdmlwdG4xMjAxQGdtYWlsLmNvbSJ9.9QnwmLQp5I3q-q_KruyzeMBJWsXqPnifrhmrB3m3ryw";
        String wanted = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGFpdmlwdG4xMjAxQGdtYWlsLmNvbSJ9.9QnwmLQp5I3q-q_KruyzeMBJWsXqPnifrhmrB3m3ryw";

        try {
            assertEquals(wanted, TokenUtil.extractTokenFromHeader(bearToken));
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void extractTokenFromHeaderShouldThrow() {
        String throwableToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGFpdmlwdG4xMjAxQGdaYWlsLmNvbSJ9.9QnwmLQp5I3q-q_KruyzeMBJWsXqPnifrhmrB3m3ryw";

        assertThrows(Exception.class, () -> TokenUtil.extractTokenFromHeader(throwableToken));
    }

    @Test
    void getBody() {
        String expected = "email@gmail.com";
        String token = TokenUtil.generateToken(expected);
        String actual = TokenUtil.getSubject(token);
        assertEquals(expected, actual);
    }
}
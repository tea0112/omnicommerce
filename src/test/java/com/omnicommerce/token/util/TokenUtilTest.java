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
        String bearToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGFpdmlwdG4xMjAxQGdtYWlsLmNvbSJ9.9QnwmLQp5I3q-q_KruyzeMBJWsXqPnifrhmrB3m3ryw";

        assertThrows(Exception.class, () -> TokenUtil.extractTokenFromHeader(bearToken));
    }
}
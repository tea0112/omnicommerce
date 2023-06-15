package com.omnicommerce.token.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TokenUtilTest {

    @Test
    void extractTokenFromHeaderPass() {
        String bearToken = "Bear eyJhbGciOiJIUzI1NiJ9.77-9InN1YiI6ImZhZmFmYWZhZn3vv70fdu-_vQZ177-9Fu-_ve-_ve-_vTbvv73vv70n.uJuZ5spIJlD0tQWcXExusCBYUfnAiMGcI5KAueqITpU";
        String wanted = "eyJhbGciOiJIUzI1NiJ9.77-9InN1YiI6ImZhZmFmYWZhZn3vv70fdu-_vQZ177-9Fu-_ve-_ve-_vTbvv73vv70n.uJuZ5spIJlD0tQWcXExusCBYUfnAiMGcI5KAueqITpU";

        try {
            assertEquals(wanted, TokenUtil.extractTokenFromHeader(bearToken));
        } catch (Exception ex) {
            assertNull(ex);
        }
    }

    @Test
    void extractTokenFromHeaderShouldThrow() {
        String throwableToken = "eyJhbGciOiJIUzI1NiJ9.77-9InN1YiI6ImZhZmFmYWZhZn3vv70fdu-_vQZ177-9Fu-_ve-_ve-_vTbvv73vv70n.uJuZ5spIJlD0tQWcXExusCBYUfnAiMGcI5KAueqITpU";

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
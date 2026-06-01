package gestiontransports.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generateToken_extractEmail_retourneEmailCorrect() {
        String token = jwtUtil.generateToken("test@test.com", List.of("COLLABORATEUR"));
        assertEquals("test@test.com", jwtUtil.extractEmail(token));
    }

    @Test
    void isTokenValid_tokenValide_retourneTrue() {
        String token = jwtUtil.generateToken("test@test.com", List.of("COLLABORATEUR"));
        assertTrue(jwtUtil.isTokenValid(token, "test@test.com"));
    }

    @Test
    void isTokenValid_mauvaisEmail_retourneFalse() {
        String token = jwtUtil.generateToken("test@test.com", List.of("COLLABORATEUR"));
        assertFalse(jwtUtil.isTokenValid(token, "autre@test.com"));
    }
}

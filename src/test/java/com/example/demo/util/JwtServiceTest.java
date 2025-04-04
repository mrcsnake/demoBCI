package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;
    private String secret; 
    private String email = "test@example.com";
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Generar una clave segura y codificarla en Base64
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        ReflectionTestUtils.setField(jwtService, "secret", secret);
        userDetails = new User(email, "", java.util.Collections.emptyList());
    }

    @Test
    void extraerUsername_Success() {
        String token = jwtService.generarToken(email);
        String username = jwtService.extraerUsername(token);
        assertEquals(email, username);
    }

    @Test
    void extraerExpiracion_Success() {
        String token = jwtService.generarToken(email);
        Date expiration = jwtService.extraerExpiracion(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void extraerClaim_Success() {
        String token = jwtService.generarToken(email);
        String subject = jwtService.extraerClaim(token, Claims::getSubject);
        assertEquals(email, subject);
    }

    @Test
    void isTokenExpirado_False() {
        String token = jwtService.generarToken(email);
        boolean expired = ReflectionTestUtils.invokeMethod(jwtService, "isTokenExpirado", token);
        assertFalse(expired);
    }

    @Test
    void isTokenValido_True() {
        String token = jwtService.generarToken(email); // Usar email para generar el token
        boolean valid = jwtService.isTokenValido(token, userDetails);
        assertTrue(valid);
    }

    @Test
    void isTokenValido_False_UsernameMismatch() {
        UserDetails otherUserDetails = new User("other@example.com", "", java.util.Collections.emptyList());
        String token = jwtService.generarToken(email);
        boolean valid = jwtService.isTokenValido(token, otherUserDetails);
        assertFalse(valid);
    }

    @Test
    void generarToken_WithClaims_Success() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        String token = jwtService.generarToken(extraClaims, userDetails);
        assertNotNull(token);
    }

    @Test
    void generarToken_WithEmail_Success() {
        String token = jwtService.generarToken(email);
        assertNotNull(token);
    }
}
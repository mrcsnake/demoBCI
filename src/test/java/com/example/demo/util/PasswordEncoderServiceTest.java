package com.example.demo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderServiceTest {

    private PasswordEncoderService passwordEncoderService;

    @BeforeEach
    void setUp() {
        passwordEncoderService = new PasswordEncoderService();
    }

    @Test
    void encodePassword_Success() {
        String rawPassword = "password123";
        String encodedPassword = passwordEncoderService.encodePassword(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$"));
    }

    @Test
    void encodePassword_DifferentEncodings() {
        String rawPassword = "password123";
        String encodedPassword1 = passwordEncoderService.encodePassword(rawPassword);
        String encodedPassword2 = passwordEncoderService.encodePassword(rawPassword);

        assertNotNull(encodedPassword1);
        assertNotNull(encodedPassword2);
        assertNotEquals(encodedPassword1, encodedPassword2);
    }

    @Test
    void encodePassword_EmptyPassword() {
        String rawPassword = "";
        String encodedPassword = passwordEncoderService.encodePassword(rawPassword);

        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$"));
    }

    @Test
    void encodePassword_NullPassword() {
        String rawPassword = null;
        assertThrows(IllegalArgumentException.class, () -> passwordEncoderService.encodePassword(rawPassword));
    }
}
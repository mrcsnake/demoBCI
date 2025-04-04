package com.example.demo.controller;

import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.ILoginService;
import com.example.demo.service.IRegistraUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IRegistraUsuarioService registraUsuario;

    @Mock
    private ILoginService loginService;

    @InjectMocks
    private AuthController authController;

    private RegisterDTO registerDTO;
    private AuthRequestDTO authRequestDTO;
    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    void setUp() {
        registerDTO = RegisterDTO.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .build();

        authRequestDTO = new AuthRequestDTO("test@example.com", "password");

        authResponseDTO = AuthResponseDTO.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .token("testToken")
                .isActive(true)
                .build();
    }

    @Test
    void registrarUsuario_Success() {
        when(registraUsuario.registrarUsuario(any(RegisterDTO.class))).thenReturn(authResponseDTO);

        ResponseEntity<?> response = authController.registrarUsuario(registerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(authResponseDTO, response.getBody());
    }

    @Test
    void registrarUsuario_UserAlreadyExists() {
        when(registraUsuario.registrarUsuario(any(RegisterDTO.class))).thenThrow(new UserAlreadyExistsException("User already exists"));

        ResponseEntity<?> response = authController.registrarUsuario(registerDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void registrarUsuario_BadRequest() {
        when(registraUsuario.registrarUsuario(any(RegisterDTO.class))).thenThrow(new IllegalArgumentException("Invalid argument"));

        ResponseEntity<?> response = authController.registrarUsuario(registerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody());
    }

    @Test
    void autenticarUsuario_Success() {
        when(loginService.login(any(AuthRequestDTO.class), any(AuthenticationManager.class))).thenReturn(authResponseDTO);

        ResponseEntity<?> response = authController.autenticarUsuario(authRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponseDTO, response.getBody());
    }

    @Test
    void autenticarUsuario_UserNotFound() {
        when(loginService.login(any(AuthRequestDTO.class), any(AuthenticationManager.class))).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<?> response = authController.autenticarUsuario(authRequestDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void autenticarUsuario_Unauthorized() {
        when(loginService.login(any(AuthRequestDTO.class), any(AuthenticationManager.class))).thenThrow(new AuthException("Invalid credentials"));

        ResponseEntity<?> response = authController.autenticarUsuario(authRequestDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void handleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "errorMessage");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = authController.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("fieldName", "errorMessage"), response.getBody());
    }
}
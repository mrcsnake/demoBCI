package com.example.demo.controller;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.service.IUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private IUsuarioService usuarioService;

    @InjectMocks
    private UserController userController;

    private List<AuthResponseDTO> usuarios;

    @BeforeEach
    void setUp() {
        usuarios = Arrays.asList(
                AuthResponseDTO.builder()
                        .id(UUID.randomUUID())
                        .name("User 1")
                        .email("user1@example.com")
                        .created(LocalDateTime.now())
                        .modified(LocalDateTime.now())
                        .lastLogin(LocalDateTime.now())
                        .token("token1")
                        .isActive(true)
                        .build(),
                AuthResponseDTO.builder()
                        .id(UUID.randomUUID())
                        .name("User 2")
                        .email("user2@example.com")
                        .created(LocalDateTime.now())
                        .modified(LocalDateTime.now())
                        .lastLogin(LocalDateTime.now())
                        .token("token2")
                        .isActive(true)
                        .build()
        );
    }

    @Test
    void buscarTodos_Success() {
        when(usuarioService.findAll()).thenReturn(usuarios);

        ResponseEntity<?> response = userController.buscarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarios, response.getBody());

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void buscarTodos_EmptyList() {
        when(usuarioService.findAll()).thenReturn(List.of());

        ResponseEntity<?> response = userController.buscarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(), response.getBody());

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void buscarTodos_NullList() {
        when(usuarioService.findAll()).thenReturn(null);

        ResponseEntity<?> response = userController.buscarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());

        verify(usuarioService, times(1)).findAll();
    }
}
package com.example.demo.util;

import com.example.demo.model.Usuario;
import com.example.demo.service.ILoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioDetailsServiceTest {

    @Mock
    private ILoginService iLoginService;

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
    }

    @Test
    void loadUserByUsername_Success() {
        when(iLoginService.buscarPorEmail(usuario.getEmail())).thenReturn(usuario);

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(usuario.getEmail());

        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(iLoginService.buscarPorEmail(usuario.getEmail())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> usuarioDetailsService.loadUserByUsername(usuario.getEmail()));
    }

    @Test
    void loadUserByUsername_NullEmail() {
        assertThrows(UsernameNotFoundException.class, () -> usuarioDetailsService.loadUserByUsername(null));
    }

    @Test
    void loadUserByUsername_EmptyEmail() {
        assertThrows(UsernameNotFoundException.class, () -> usuarioDetailsService.loadUserByUsername(""));
    }
}
package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.exception.AuthException;
import com.example.demo.mapper.LoginMapper;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.util.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Spy
    private LoginMapper loginMapper = LoginMapper.INSTANCE;

    @InjectMocks
    private LoginServiceImpl loginService;

    private AuthRequestDTO authRequestDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail("test@example.com");
        authRequestDTO.setPassword("password123");

        usuario = new Usuario();
        usuario.setEmail(authRequestDTO.getEmail());
        usuario.setPassword(authRequestDTO.getPassword());
    }

    @Test
    void login_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generarToken(authRequestDTO.getEmail())).thenReturn("jwtToken");
        when(usuarioRepository.findByEmail(authRequestDTO.getEmail())).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        AuthResponseDTO authResponseDTO = loginService.login(authRequestDTO, authenticationManager);

        assertNotNull(authResponseDTO);
        assertEquals("jwtToken", authResponseDTO.getToken());
        assertEquals(authRequestDTO.getEmail(), authResponseDTO.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void login_InvalidCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(AuthException.class, () -> loginService.login(authRequestDTO, authenticationManager));
    }

    @Test
    void guardarUsuarioLastLogin_Success() {
        when(usuarioRepository.findByEmail(authRequestDTO.getEmail())).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Se usa reflection para acceder al método privado
        ReflectionTestUtils.invokeMethod(loginService, "guardarUsuarioLastLogin", authRequestDTO.getEmail());

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void buscarPorEmail_Success() {
        when(usuarioRepository.findByEmail(authRequestDTO.getEmail())).thenReturn(usuario);

        Usuario result = loginService.buscarPorEmail(authRequestDTO.getEmail());

        assertEquals(usuario, result);
    }
}
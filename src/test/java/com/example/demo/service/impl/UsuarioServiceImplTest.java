package com.example.demo.service.impl;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.mapper.LoginMapper;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Spy
    private LoginMapper loginMapper = LoginMapper.INSTANCE;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private List<Usuario> usuarios;

    @BeforeEach
    void setUp() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(UUID.randomUUID());
        usuario1.setName("Test User 1");
        usuario1.setEmail("test1@example.com");
        usuario1.setCreated(LocalDateTime.now());
        usuario1.setLastLogin(LocalDateTime.now());
        usuario1.setToken("token1");
        usuario1.setActive(true);

        Usuario usuario2 = new Usuario();
        usuario2.setId(UUID.randomUUID());
        usuario2.setName("Test User 2");
        usuario2.setEmail("test2@example.com");
        usuario2.setCreated(LocalDateTime.now());
        usuario2.setLastLogin(LocalDateTime.now());
        usuario2.setToken("token2");
        usuario2.setActive(true);

        usuarios = Arrays.asList(usuario1, usuario2);
    }

    @Test
    void findAll_Success() {
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<AuthResponseDTO> authResponses = usuarioService.findAll();

        assertEquals(usuarios.size(), authResponses.size());
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            AuthResponseDTO authResponse = authResponses.get(i);
            assertEquals(usuario.getId(), authResponse.getId());
            assertEquals(usuario.getName(), authResponse.getName());
            assertEquals(usuario.getEmail(), authResponse.getEmail());
            assertEquals(usuario.getCreated(), authResponse.getCreated());
            assertEquals(usuario.getLastLogin(), authResponse.getLastLogin());
            assertEquals(usuario.getToken(), authResponse.getToken());
            assertEquals(usuario.isActive(), authResponse.isActive());
        }
    }
}
package com.example.demo.service.impl;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.PhoneDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.model.Telefono;
import com.example.demo.model.Usuario;
import com.example.demo.repository.TelefonoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.util.JwtService;
import com.example.demo.util.PasswordEncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrarUsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TelefonoRepository telefonoRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    @Mock
    private JwtService jwtService;

    @Spy
    private UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;

    @InjectMocks
    private RegistrarUsuarioServiceImpl registrarUsuarioService;

    private RegisterDTO registerDTO;
    private Usuario usuario;
    private PhoneDTO phoneDTO;
    private List<PhoneDTO> phoneDTOs;

    @BeforeEach
    void setUp() {
        phoneDTO = new PhoneDTO();
        phoneDTOs = Collections.singletonList(phoneDTO);

        registerDTO = new RegisterDTO("Test User", "test@example.com", "Password123@", phoneDTOs);
        usuario = UsuarioMapper.INSTANCE.registerDTOToUsuario(registerDTO);

        ReflectionTestUtils.setField(registrarUsuarioService, "emailRegex", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        ReflectionTestUtils.setField(registrarUsuarioService, "passwordRegex", "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    @Test
    void registrarUsuario_Success() {
        when(usuarioRepository.findByEmail(registerDTO.getEmail())).thenReturn(null);
        when(jwtService.generarToken(registerDTO.getEmail())).thenReturn("jwtToken");
        when(passwordEncoderService.encodePassword(registerDTO.getPassword())).thenReturn("encodedPassword");

        Usuario usuarioGuardado = UsuarioMapper.INSTANCE.registerDTOToUsuario(registerDTO);
        usuarioGuardado.setToken("jwtToken");
        usuarioGuardado.setCreated(LocalDateTime.now());
        usuarioGuardado.setLastLogin(LocalDateTime.now());
        usuarioGuardado.setActive(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        when(telefonoRepository.saveAll(anyList())).thenReturn(UsuarioMapper.INSTANCE.phonesToTelefonos(phoneDTOs));

        AuthResponseDTO authResponseDTO = registrarUsuarioService.registrarUsuario(registerDTO);

        assertNotNull(authResponseDTO);
        assertEquals(registerDTO.getEmail(), authResponseDTO.getEmail());
        assertEquals("jwtToken", authResponseDTO.getToken());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(telefonoRepository).saveAll(anyList());
    }

    @Test
    void registrarUsuario_UserAlreadyExists() {
        when(usuarioRepository.findByEmail(registerDTO.getEmail())).thenReturn(usuario);

        assertThrows(UserAlreadyExistsException.class, () -> registrarUsuarioService.registrarUsuario(registerDTO));
    }

    @Test
    void registrarUsuario_InvalidEmailFormat() {
        registerDTO = new RegisterDTO("Test User", "invalid-email", "Password123@", phoneDTOs);

        assertThrows(IllegalArgumentException.class, () -> registrarUsuarioService.registrarUsuario(registerDTO));
    }

    @Test
    void registrarUsuario_InvalidPasswordFormat() {
        registerDTO = new RegisterDTO("Test User", "test@example.com", "invalid", phoneDTOs);

        assertThrows(IllegalArgumentException.class, () -> registrarUsuarioService.registrarUsuario(registerDTO));
    }

    @Test
    void guardarUsuario_Success() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generarToken(usuario.getEmail())).thenReturn("jwtToken");
        when(passwordEncoderService.encodePassword(usuario.getPassword())).thenReturn("encodedPassword");

        ReflectionTestUtils.invokeMethod(registrarUsuarioService, "guardarUsuario", usuario);

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void guardarTelefonos_Success() {
        ReflectionTestUtils.invokeMethod(registrarUsuarioService, "guardarTelefonos", usuario);

        verify(telefonoRepository).saveAll(anyList());
    }

    @Test
    void validarUsuario_Success() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(null);

        ReflectionTestUtils.invokeMethod(registrarUsuarioService, "validarUsuario", usuario);

        verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    void validarUsuario_UserAlreadyExists() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);

        assertThrows(UserAlreadyExistsException.class, () -> ReflectionTestUtils.invokeMethod(registrarUsuarioService, "validarUsuario", usuario));
    }

    @Test
    void validarUsuario_InvalidEmailFormat() {
        usuario.setEmail("invalid-email");

        assertThrows(IllegalArgumentException.class, () -> ReflectionTestUtils.invokeMethod(registrarUsuarioService, "validarUsuario", usuario));
    }

    @Test
    void validarUsuario_InvalidPasswordFormat() {
        usuario.setPassword("invalid");

        assertThrows(IllegalArgumentException.class, () -> ReflectionTestUtils.invokeMethod(registrarUsuarioService, "validarUsuario", usuario));
    }
}
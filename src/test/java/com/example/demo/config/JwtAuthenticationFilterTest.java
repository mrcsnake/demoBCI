package com.example.demo.config;

import com.example.demo.util.JwtService;
import com.example.demo.util.UsuarioDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private String validToken = "validToken";
    private String username = "test@example.com";
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new User(username, "password", Collections.emptyList());
        SecurityContextHolder.clearContext(); // Limpiar el contexto de seguridad antes de cada prueba
    }

    @Test
    void doFilterInternal_ValidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.extraerUsername(validToken)).thenReturn(username);
        when(usuarioDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValido(validToken, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.extraerUsername(validToken)).thenThrow(new JwtException("Invalid token"));
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setCharacterEncoding(StandardCharsets.UTF_8.name());
        verify(writer).write("Token JWT inválido");
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_UsernameNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.extraerUsername(validToken)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_TokenNotValid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.extraerUsername(validToken)).thenReturn(username);
        when(usuarioDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValido(validToken, userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
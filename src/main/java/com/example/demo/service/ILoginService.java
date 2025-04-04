package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;

import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.model.Usuario;

/**
 * Interfaz que define los servicios para la autenticación de usuarios.
 * Esta interfaz declara los métodos para realizar el inicio de sesión de un usuario
 * y buscar un usuario por su dirección de correo electrónico.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public interface ILoginService {

	 /**
     * Realiza el inicio de sesión de un usuario.
     *
     * @param authRequest           Objeto que contiene las credenciales de autenticación (email y contraseña).
     * @param authenticationManager Administrador de autenticación de Spring Security.
     * @return AuthResponseDTO Objeto que contiene la información del usuario autenticado y el token JWT.
     */
    AuthResponseDTO login(AuthRequestDTO authRequest, AuthenticationManager authenticationManager);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico del usuario a buscar.
     * @return Usuario Objeto usuario encontrado.
     */
    Usuario buscarPorEmail(String email);
}

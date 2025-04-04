package com.example.demo.service;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.RegisterDTO;

/**
 * Interfaz que define los servicios para el registro de nuevos usuarios.
 * Esta interfaz declara el método para registrar un nuevo usuario en el sistema.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public interface IRegistraUsuarioService {

	/**
     * Registra un nuevo usuario en el sistema.
     *
     * @param registerDTO Datos del usuario a registrar.
     * @return AuthResponseDTO con la información del usuario registrado y su token.
     */
	AuthResponseDTO registrarUsuario(RegisterDTO registerDTO);
}

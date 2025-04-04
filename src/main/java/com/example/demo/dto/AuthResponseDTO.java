package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa la respuesta de autenticación.
 * Este objeto se utiliza para transferir información del usuario autenticado,
 * incluyendo su ID, nombre, email, fechas de creación, modificación y último login,
 * el token de autenticación y el estado de activación.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Builder
@AllArgsConstructor
@Data
public class AuthResponseDTO {
	/**
     * Identificador único del usuario.
     */
    private UUID id;

    /**
     * Nombre del usuario.
     */
    private String name;

    /**
     * Dirección de correo electrónico del usuario.
     */
    private String email;

    /**
     * Fecha y hora de creación del usuario.
     */
    private LocalDateTime created;

    /**
     * Fecha y hora de última modificación de los datos del usuario.
     */
    private LocalDateTime modified;

    /**
     * Fecha y hora del último inicio de sesión del usuario.
     */
    private LocalDateTime lastLogin;

    /**
     * Token de autenticación del usuario.
     */
    private String token;

    /**
     * Indica si el usuario está activo (true) o inactivo (false).
     */
    private boolean isActive;
}
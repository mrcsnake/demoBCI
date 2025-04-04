package com.example.demo.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Entidad JPA que representa a un usuario en la base de datos.
 * Esta entidad almacena la información del usuario, incluyendo su ID, nombre, email, contraseña,
 * fechas de creación, modificación y último login, token de autenticación, estado de actividad
 * y la lista de números de teléfono asociados.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Entity
@Data
public class Usuario {
    
	/**
     * Identificador único del usuario.
     * Generado automáticamente usando UUID.
     */
    @Id
    @GeneratedValue
    @UuidGenerator
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
     * Contraseña del usuario.
     */
    private String password;

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

    /**
     * Lista de números de teléfono asociados al usuario.
     * Relación uno-a-muchos con la entidad Telefono.
     * La relación está mapeada por el campo "usuario" en la entidad Telefono.
     * Se utiliza CascadeType.ALL para propagar todas las operaciones a los teléfonos asociados.
     * Se utiliza orphanRemoval = true para eliminar los teléfonos que ya no están asociados al usuario.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefono> phones;
}
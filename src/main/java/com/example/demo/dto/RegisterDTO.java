package com.example.demo.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


/**
 * DTO (Data Transfer Object) que representa los datos para el registro de un nuevo usuario.
 * Este objeto se utiliza para transferir la información necesaria para registrar un usuario,
 * incluyendo su nombre, email, contraseña y lista de teléfonos.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Builder
@AllArgsConstructor
@Data
public class RegisterDTO {

	 /**
     * Nombre del usuario.
     * No puede estar en blanco.
     * @NotBlank Indica que el campo no puede estar en blanco.
     */
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;

    /**
     * Dirección de correo electrónico del usuario.
     * No puede estar en blanco y debe ser una dirección de correo electrónico válida.
     * @NotBlank Indica que el campo no puede estar en blanco.
     * @Email Indica que el campo debe ser una dirección de correo electrónico válida.
     */
    @NotBlank(message = "El email no puede estar en blanco")
    @Email(message = "El email debe ser válido")
    private String email;

    /**
     * Contraseña del usuario.
     * No puede estar en blanco.
     * @NotBlank Indica que el campo no puede estar en blanco.
     */
    @NotBlank(message = "La contraseña no puede estar en blanco")
    private String password;

    /**
     * Lista de números de teléfono del usuario.
     * Puede ser nula o vacía.
     */
    private List<PhoneDTO> phones;
}

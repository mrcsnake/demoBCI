package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO (Data Transfer Object) que representa una solicitud de autenticación.
 * * Este objeto se utiliza para transferir las credenciales de un usuario (email y password)
 * desde el cliente al servidor durante el proceso de autenticación.
 * * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {
	
	/**
	 * Dirección de correo electrónico del usuario.
	 * * No puede estar en blanco.
	 * * @NotBlank Indica que el campo no puede estar en blanco.
	 */
	@NotBlank(message = "El email no puede estar en blanco")
    private String email;
	
	/**
	 * Contraseña del usuario.
	 * * No puede estar en blanco.
	 * * @NotBlank Indica que el campo no puede estar en blanco.
	 */
	@NotBlank(message = "la password no puede estar en blanco")
    private String password;

}

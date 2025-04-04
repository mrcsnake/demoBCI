package com.example.demo.exception;

/**
 * Excepción  para indicar que un usuario ya existe.
 * Esta excepción se lanza cuando se intenta registrar un usuario con un correo electrónico
 * que ya está registrado en el sistema.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public class UserAlreadyExistsException extends RuntimeException {
    
	/**
     * Constructor de la excepción UserAlreadyExistsException.
     *
     * @param message El mensaje de error que describe la excepción.
     */
	public UserAlreadyExistsException(String message) {
        super(message);
    }
	
	
}
package com.example.demo.exception;

/**
 * Excepción para indicar que un usuario no fue encontrado.
 * Esta excepción se lanza cuando se intenta acceder o modificar un usuario
 * que no existe en el sistema.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public class UserNotFoundException extends RuntimeException {
	

    /**
     * Constructor de la excepción UserNotFoundException.
     *
     * @param message El mensaje de error que describe la excepción.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
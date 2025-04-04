package com.example.demo.exception;

/**
 * Excepción para errores de autenticación.
 * Esta excepción se lanza cuando ocurren errores durante el proceso de autenticación,
 * como credenciales inválidas o tokens incorrectos.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public class AuthException extends RuntimeException {
	
	/**
     * Constructor de la excepción AuthException.
     *
     * @param message El mensaje de error que describe la excepción.
     */
    public AuthException(String message) {
        super(message);
    }
}
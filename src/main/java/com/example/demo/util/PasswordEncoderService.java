package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para la codificación de contraseñas utilizando BCrypt.
 * Este servicio proporciona un método para codificar contraseñas de forma segura.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Service
public class PasswordEncoderService {
	/**
    * Codificador de contraseñas de Spring Security.
    */
   private final PasswordEncoder passwordEncoder;

   /**
    * Constructor de la clase PasswordEncoderService.
    * Inicializa el codificador de contraseñas con BCrypt.
    */
   public PasswordEncoderService() {
       this.passwordEncoder = new BCryptPasswordEncoder();
   }

   /**
    * Codifica una contraseña utilizando BCrypt.
    *
    * @param password La contraseña a codificar.
    * @return La contraseña codificada.
    */
   public String encodePassword(String password) {
       return passwordEncoder.encode(password);
   }
}
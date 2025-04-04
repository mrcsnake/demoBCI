package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.service.ILoginService;

/**
 * Servicio para la carga de detalles de usuario para la autenticación de Spring Security.
 * Implementa la interfaz UserDetailsService para proporcionar la lógica de carga de usuarios
 * basada en su dirección de correo electrónico.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

	/**
     * Servicio para la lógica de inicio de sesión y búsqueda de usuarios.
     */
    @Autowired
    private ILoginService iLoginService;

    /**
     * Carga los detalles de un usuario por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico del usuario.
     * @return UserDetails Objeto UserDetails que representa al usuario.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el email dado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = iLoginService.buscarPorEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }
        return new User(usuario.getEmail(), usuario.getPassword(), java.util.Collections.emptyList());
    }
}
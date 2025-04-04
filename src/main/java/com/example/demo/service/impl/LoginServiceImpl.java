package com.example.demo.service.impl;

import java.time.LocalDateTime;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.exception.AuthException;
import com.example.demo.mapper.LoginMapper;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.ILoginService;
import com.example.demo.util.JwtService;

import lombok.AllArgsConstructor;


/**
 * Implementación de la interfaz ILoginService.
 * Esta clase proporciona la lógica de negocio para la autenticación de usuarios.
 * Utiliza el repositorio UsuarioRepository para acceder a la base de datos,
 * el servicio JwtService para generar tokens JWT y el AuthenticationManager de Spring Security
 * para realizar la autenticación.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@AllArgsConstructor
@Service
public class LoginServiceImpl implements ILoginService {

	/**
	 * Repositorio para acceder a la entidad Usuario en la base de datos.
	 */
	private final UsuarioRepository usuarioRepository;
	
	/**
	 * Servicio para generar tokens JWT.
	 */
	private final JwtService jwtService;

	/**
	 * Implementación del método de inicio de sesión.
	 *
	 * @param authRequest           Objeto que contiene las credenciales de
	 *                              autenticación (email y contraseña).
	 * @param authenticationManager Administrador de autenticación de Spring
	 *                              Security.
	 * @return AuthResponse Objeto que contiene la información del usuario
	 *         autenticado y el token JWT.
	 * @throws AuthException Si las credenciales de autenticación son inválidas.
	 */
	@Override
	public AuthResponseDTO login(AuthRequestDTO authRequest, AuthenticationManager authenticationManager) {
		try {
			String email = authRequest.getEmail();
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, authRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwtToken = jwtService.generarToken(email);

			Usuario usuarioGuardado = guardarUsuarioLastLogin(email);

			AuthResponseDTO authResponse = LoginMapper.INSTANCE.usuarioToAuthResponseDTO(usuarioGuardado);
			authResponse.setToken(jwtToken);
			return authResponse;
		} catch (AuthenticationException e) {
			throw new AuthException("Credenciales inválidas");
		}
	}

	/**
     * Actualiza la fecha de último inicio de sesión del usuario y guarda los cambios en la base de datos.
     *
     * @param email Email del usuario.
     * @return Usuario Objeto usuario con el ultimo login actualizado.
     */
	private Usuario guardarUsuarioLastLogin(String email) {
		Usuario usuario = buscarPorEmail(email);
		usuario.setLastLogin(LocalDateTime.now());
		return usuarioRepository.save(usuario);
	}

	/**
     * Busca un usuario por su email.
     *
     * @param email Email del usuario a buscar.
     * @return Usuario Objeto usuario encontrado.
     */
	@Override
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

}

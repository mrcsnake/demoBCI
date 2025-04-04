package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.util.JwtService;
import com.example.demo.util.UsuarioDetailsService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filtro de autenticación JWT. Este filtro intercepta las solicitudes HTTP y
 * verifica la presencia de un token JWT válido en el encabezado Authorization.
 * Si el token es válido, establece la autenticación en el contexto de seguridad
 * de Spring Security.
 * 
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	/**
	 * Servicio para la generación y validación de tokens JWT.
	 */
	@Autowired
	private JwtService jwtService;

	/**
	 * Servicio para la carga de detalles de usuario.
	 */
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;

	/**
	 * Realiza el filtrado de la solicitud HTTP.
	 *
	 * @param request     La solicitud HTTP.
	 * @param response    La respuesta HTTP.
	 * @param filterChain La cadena de filtros.
	 * @throws ServletException Si ocurre un error en el servlet.
	 * @throws IOException      Si ocurre un error de entrada/salida.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		try {
			username = jwtService.extraerUsername(jwt);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.usuarioDetailsService.loadUserByUsername(username);
				if (jwtService.isTokenValido(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
			filterChain.doFilter(request, response);
		} catch (JwtException e) {
			// Se maneja la excepción cuando el token es inválido o no existe
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token JWT inválido");
		}
	}
}

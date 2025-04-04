package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para la generación y validación de tokens JWT (JSON Web Tokens).
 * Este servicio proporciona métodos para generar tokens JWT, extraer información de los tokens
 * y validar su validez.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Service
public class JwtService {
	
	/**
     * Clave secreta utilizada para firmar los tokens JWT.
     * Se inyecta desde la configuración de la aplicación.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     *
     * @param token El token JWT.
     * @return El nombre de usuario extraído del token.
     */
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token El token JWT.
     * @return La fecha de expiración del token.
     */
    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token JWT utilizando un resolver de claims.
     *
     * @param token           El token JWT.
     * @param claimsResolver  Función para resolver el claim deseado.
     * @param <T>             Tipo del claim a extraer.
     * @return El claim extraído del token.
     */
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token JWT.
     *
     * @param token El token JWT.
     * @return El objeto Claims que contiene todos los claims del token.
     */
    private Claims extraerTodosClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si el token JWT ha expirado.
     *
     * @param token El token JWT.
     * @return true si el token ha expirado, false en caso contrario.
     */
    private boolean isTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    /**
     * Verifica si el token JWT es válido para un usuario específico.
     *
     * @param token       El token JWT.
     * @param userDetails Detalles del usuario.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean isTokenValido(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
    }

    /**
     * Genera un token JWT para un email dado.
     *
     * @param email El email del usuario.
     * @return El token JWT generado.
     */
    public String generarToken(String email) {
        UserDetails userDetails = new User(email, "", java.util.Collections.emptyList()); 

        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 horas
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Genera un token JWT con claims adicionales para un usuario dado.
     *
     * @param extraClaims   Claims adicionales para incluir en el token.
     * @param userDetails   Detalles del usuario.
     * @return El token JWT generado.
     */
    public String generarToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 horas
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}

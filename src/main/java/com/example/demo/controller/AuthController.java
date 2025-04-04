package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.ILoginService;
import com.example.demo.service.IRegistraUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Controlador REST para la autenticación y registro de usuarios.
 * Este controlador maneja las solicitudes de registro y autenticación de usuarios.
 * Utiliza los servicios IRegistraUsuarioService e ILoginService para realizar las operaciones correspondientes.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	/**
     * Administrador de autenticación de Spring Security.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Servicio para registrar nuevos usuarios.
     */
    private final IRegistraUsuarioService registraUsuario;

    /**
     * Servicio para la autenticación de usuarios.
     */
    private final ILoginService loginService;

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param registerDTO Objeto que contiene la información del usuario a registrar.
     * @return ResponseEntity<?> Respuesta HTTP con el resultado del registro.
     * @throws Exception Si ocurre un error durante el registro.
     */
	@PostMapping("/register")
	@Operation(summary = "Registrar un nuevo usuario")
	@ApiResponse(responseCode = "201", description = "Usuario registrado con éxito",
    content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))
    })
	@ApiResponse(responseCode = "400", description = "Formato de correo electrónico o contraseña inválido", content = @Content(schema = @Schema(example = "Formato de correo electrónico inválido")))
	@ApiResponse(responseCode = "409", description = "El correo ya está registrado", content = @Content(schema = @Schema(example = "El correo ya registrado")))
	public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegisterDTO registerDTO) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(registraUsuario.registrarUsuario(registerDTO));
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
     * Endpoint para la autenticación de un usuario.
     *
     * @param authRequest Objeto que contiene las credenciales de autenticación.
     * @return ResponseEntity<?> Respuesta HTTP con el resultado de la autenticación.
     */
	@PostMapping("/login")
	@Operation(summary = "Autenticar usuario")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud ingresada"),
            @ApiResponse(responseCode = "401", description = "Token incorrecto", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content(schema = @Schema(example = "Credenciales inválidas")))
    })
    
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(example = "Usuario no encontrado")))
	public ResponseEntity<?> autenticarUsuario(@Valid @RequestBody AuthRequestDTO authRequest) {
		try {
			return ResponseEntity.ok(loginService.login(authRequest, authenticationManager));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (AuthException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	
	/**
     * Manejador de excepciones para errores de validación de argumentos de métodos.
     *
     * @param ex Excepción MethodArgumentNotValidException.
     * @return ResponseEntity<Map<String, String>> Respuesta HTTP con los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}

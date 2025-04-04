package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.IUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * Controlador REST para la gestión de usuarios.
 * Este controlador maneja las solicitudes relacionadas con la obtención de información de usuarios.
 * Utiliza el servicio IUsuarioService para realizar las operaciones correspondientes.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints para la gestión de usuarios")
@AllArgsConstructor
public class UserController {

	/**
     * Servicio que proporciona la lógica de negocio para los usuarios.
     */
    private final IUsuarioService usuarioService;

    /**
     * Endpoint para obtener todos los usuarios.
     *
     * @return ResponseEntity<?> Respuesta HTTP con la lista de usuarios o un error.
     */
	@GetMapping("/findAll")
	@Operation(summary = "Obtener todos los usuarios")
	@ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito")
	@ApiResponse(responseCode = "401", description = "No autorizado")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> buscarTodos() {
		return ResponseEntity.ok(usuarioService.findAll());
	}
}

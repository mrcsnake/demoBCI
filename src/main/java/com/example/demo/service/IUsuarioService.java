package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.AuthResponseDTO;

/**
 * Interfaz que define los servicios para la gestión de usuarios.
 * Esta interfaz declara el método para obtener una lista de todos los usuarios.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public interface IUsuarioService {
	
	/**
     * Recupera una lista de todos los usuarios.
     *
     * @return List<AuthResponseDTO> Lista de objetos AuthResponseDTO que representan a los usuarios.
     */
	List<AuthResponseDTO> findAll();
}

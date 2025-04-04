package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.mapper.LoginMapper;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.IUsuarioService;

import lombok.AllArgsConstructor;

/**
 * Implementación de la interfaz IUsuarioService.
 * Esta clase proporciona la lógica de negocio para la gestión de usuarios.
 * Utiliza el repositorio UsuarioRepository para acceder a la base de datos
 * y el mapper LoginMapper para mapear entidades Usuario a objetos AuthResponseDTO.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

	/**
     * Repositorio para acceder a la entidad Usuario en la base de datos.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Recupera una lista de todos los usuarios y los mapea a objetos AuthResponse.
     *
     * @return List<AuthResponse> Lista de objetos AuthResponse que representan a los usuarios.
     */
	@Override
	public List<AuthResponseDTO> findAll() {
		List<AuthResponseDTO> authResponses = new ArrayList<>();
		List<Usuario> usuarios = usuarioRepository.findAll();
		for (Usuario usuario : usuarios) {
			authResponses.add(LoginMapper.INSTANCE.usuarioToAuthResponseDTO(usuario));
		}
		return authResponses;
	}
}

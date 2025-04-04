package com.example.demo.mapper;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Interfaz Mapper para convertir objetos Usuario a AuthResponseDTO. Utiliza
 * MapStruct para la generación automática de código de mapeo.
 * 
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Mapper(componentModel = "spring")
public interface LoginMapper {

	/**
	 * Instancia única del Mapper.
	 */
	LoginMapper INSTANCE = Mappers.getMapper(LoginMapper.class);

	/**
	 * Convierte un objeto Usuario a un objeto AuthResponseDTO.
	 *
	 * @param usuario El objeto Usuario a convertir.
	 * @return El objeto AuthResponseDTO resultante.
	 */
	@Mapping(target = "isActive", source = "active")
	AuthResponseDTO usuarioToAuthResponseDTO(Usuario usuario);
}

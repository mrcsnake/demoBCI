package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Usuario;


/** 
 * Repositorio JPA para la entidad Usuario.
 * Esta interfaz extiende JpaRepository, proporcionando métodos CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Usuario. Además, define un método personalizado para buscar un usuario por su email.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
	
	/**
     * Busca un usuario por su dirección de correo electrónico.
     * @param email La dirección de correo electrónico del usuario a buscar.
     * @return El usuario encontrado, o null si no se encuentra ningún usuario con el email dado.
     */
    Usuario findByEmail(String email);
}

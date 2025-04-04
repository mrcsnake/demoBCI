package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Telefono;

/**
 * Repositorio JPA para la entidad Telefono.
 * Esta interfaz extiende JpaRepository, proporcionando métodos CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Telefono. Spring Data JPA genera automáticamente las implementaciones de estos métodos.
  * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
public interface TelefonoRepository extends JpaRepository<Telefono, UUID> {
}

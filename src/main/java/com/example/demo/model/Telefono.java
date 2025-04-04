package com.example.demo.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Entidad JPA que representa un número de teléfono en la base de datos.
 * Esta entidad almacena la información de un número de teléfono, incluyendo su ID, número,
 * códigos de ciudad y país, y la relación con el usuario al que pertenece.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Entity
@Data
public class Telefono {
	/**
     * Identificador único del número de teléfono.
     * Generado automáticamente usando UUID.
     */
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    /**
     * Número de teléfono.
     */
    private String number;

    /**
     * Código de ciudad del número de teléfono.
     */
    private String citycode;

    /**
     * Código de país del número de teléfono.
     * Nota: Hay un error tipográfico en el nombre del campo (contrycode en lugar de countrycode).
     */
    private String contrycode;

    /**
     * Relación muchos-a-uno con la entidad Usuario.
     * Representa el usuario al que pertenece este número de teléfono.
     * La columna de unión se llama "usuario_id".
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
package com.example.demo.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) para representar la información de un número telefónico.
 * Este objeto se utiliza para transferir datos relacionados con un número de teléfono,
 * incluyendo el número en sí, el código de ciudad y el código de país.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Data
public class PhoneDTO {
	/**
     * El número de teléfono.
     * Representa la secuencia de dígitos del número telefónico.
     */
    private String number;

    /**
     * El código de ciudad del número de teléfono.
     * Representa el código de área o ciudad asociado con el número.
     */
    private String citycode;

    /**
     * El código de país del número de teléfono.
     * Representa el código de país asociado con el número.
     */
    private String countrycode;
}
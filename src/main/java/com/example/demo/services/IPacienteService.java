package com.example.demo.services;

import com.example.demo.dto.PacienteDTO;

/**
 * Servicio específico para pacientes, permitiendo CRUD y gestión de relaciones
 * mediante el método {@code update}, que maneja tanto campos básicos como
 * la asignación y desasignación de médicos.
 */
public interface IPacienteService extends GenericService<PacienteDTO, Long> {

}

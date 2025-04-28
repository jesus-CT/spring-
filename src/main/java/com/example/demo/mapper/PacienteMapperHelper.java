package com.example.demo.mapper;

import com.example.demo.models.Paciente;
import com.example.demo.repositories.PacienteRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper para MapStruct: convierte entre listas de IDs y sets de entidades.
 */
@Component
public class PacienteMapperHelper {

    private final PacienteRepository pacienteRepository;

    @Autowired
    public PacienteMapperHelper(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Convierte una lista de IDs de Paciente en un Set de entidades Paciente.
     * Ignora IDs nulos o no encontrados.
     */
    @Named("mapIdsToPacientes")
    public Set<Paciente> mapIdsToPacientes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }
        return ids.stream()
                .filter(id -> id != null)
                .map(pacienteRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    /**
     * Convierte un Set de entidades Paciente en una lista de sus IDs.
     */
    @Named("mapPacientesToIds")
    public List<Long> mapPacientesToIds(Set<Paciente> pacientes) {
        if (pacientes == null || pacientes.isEmpty()) {
            return Collections.emptyList();
        }
        return pacientes.stream()
                .map(Paciente::getId)
                .collect(Collectors.toList());
    }
}
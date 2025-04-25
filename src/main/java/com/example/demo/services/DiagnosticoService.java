package com.example.demo.services;

import com.example.demo.dto.DiagnosticoDTO;
import com.example.demo.mapper.DiagnosticoMapper;
import com.example.demo.models.Cita;
import com.example.demo.models.Diagnostico;
import com.example.demo.repositories.CitaRepository;
import com.example.demo.repositories.DiagnosticoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final DiagnosticoMapper diagnosticoMapper;
    private final CitaRepository citaRepository;

    public DiagnosticoService(DiagnosticoRepository diagnosticoRepository,
                              DiagnosticoMapper diagnosticoMapper,
                              CitaRepository citaRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.diagnosticoMapper = diagnosticoMapper;
        this.citaRepository = citaRepository;
    }

    // 1. Lectura de todos los diagnósticos
    public List<DiagnosticoDTO> getAllDiagnosticos() {
        return diagnosticoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(diagnosticoMapper::toDto)
                .collect(Collectors.toList());
    }

    // 2. Lectura de uno por ID
    public DiagnosticoDTO getDiagnosticoById(@NotNull Long id) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id " + id));
        return diagnosticoMapper.toDto(diagnostico);
    }

    // 3. Creación: eliminado (la creación se hace siempre a través de CitaService)

    // 4. Actualización: sólo actualiza los campos del diagnóstico
    public DiagnosticoDTO updateDiagnostico(@NotNull Long id, @NotNull @Valid DiagnosticoDTO dto) {
        Diagnostico existente = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id " + id));

        // MapStruct aplica valoracionEspecialista y enfermedad
        diagnosticoMapper.updateFromDto(dto, existente);

        Diagnostico updated = diagnosticoRepository.save(existente);
        return diagnosticoMapper.toDto(updated);
    }

    // 5. Borrado
    public void deleteDiagnostico(@NotNull Long id) {
        Diagnostico existente = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id " + id));

        // 1. Recupero la cita padre
        Cita cita = existente.getCita();

        // 2. Borro la cita: con cascade+orphanRemoval en Cita, se elimina también el diagnóstico
        citaRepository.delete(cita);
    }
}

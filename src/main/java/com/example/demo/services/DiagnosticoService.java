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
    private final CitaRepository citaRepository;
    private final DiagnosticoMapper diagnosticoMapper;

    public DiagnosticoService(DiagnosticoRepository diagnosticoRepository,
                              CitaRepository citaRepository,
                              DiagnosticoMapper diagnosticoMapper) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.citaRepository = citaRepository;
        this.diagnosticoMapper = diagnosticoMapper;
    }

    public List<DiagnosticoDTO> getAllDiagnosticos() {
        return diagnosticoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(diagnosticoMapper::toDto)
                .collect(Collectors.toList());
    }

    public DiagnosticoDTO getDiagnosticoById(@NotNull Long id) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id " + id));
        return diagnosticoMapper.toDto(diagnostico);
    }

    public DiagnosticoDTO createDiagnostico(@NotNull @Valid DiagnosticoDTO dto) {
        Diagnostico diagnostico = diagnosticoMapper.toEntity(dto);
        diagnostico.setId(null);

        Cita cita = citaRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cita no encontrada con id " + dto.getCitaId()));
        diagnostico.setCita(cita);

        Diagnostico saved = diagnosticoRepository.save(diagnostico);
        return diagnosticoMapper.toDto(saved);
    }

    public DiagnosticoDTO updateDiagnostico(@NotNull Long id, @NotNull @Valid DiagnosticoDTO dto) {
        Diagnostico existente = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id " + id));

        diagnosticoMapper.updateFromDto(dto, existente);

        Cita cita = citaRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cita no encontrada con id " + dto.getCitaId()));
        existente.setCita(cita);

        Diagnostico updated = diagnosticoRepository.save(existente);
        return diagnosticoMapper.toDto(updated);
    }

    public void deleteDiagnostico(@NotNull Long id) {
        Diagnostico existente = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id " + id));
        diagnosticoRepository.delete(existente);
    }
}
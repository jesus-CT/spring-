// src/main/java/com/example/demo/services/PacienteService.java
package com.example.demo.services;

import com.example.demo.dto.PacienteDTO;
import com.example.demo.mapper.PacienteMapper;
import com.example.demo.models.Paciente;
import com.example.demo.repositories.PacienteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public PacienteService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    public List<PacienteDTO> getAllPacientes() {
        return pacienteRepository.findAll(Sort.by(Sort.Direction.ASC, "apellidos")
                        .and(Sort.by("nombre")))
                .stream()
                .map(pacienteMapper::toDto)
                .collect(Collectors.toList());
    }

    public PacienteDTO getPacienteById(@NotNull Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado con id " + id));
        return pacienteMapper.toDto(paciente);
    }

    public PacienteDTO createPaciente(@NotNull @Valid PacienteDTO dto) {
        try {
            Paciente paciente = pacienteMapper.toEntity(dto);
            paciente.setId(null);
            Paciente saved = pacienteRepository.save(paciente);
            return pacienteMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario '" + dto.getUsuario() + "' ya está en uso"
            );
        }
    }

    public PacienteDTO updatePaciente(@NotNull Long id, @NotNull @Valid PacienteDTO dto) {
        Paciente existente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado con id " + id));

        // Mapeo manual de los campos del DTO sobre la entidad existente
        existente.setNombre(dto.getNombre());
        existente.setApellidos(dto.getApellidos());
        existente.setUsuario(dto.getUsuario());
        existente.setClave(dto.getClave());
        existente.setNSS(dto.getNSS());
        existente.setNumTarjeta(dto.getNumTarjeta());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());

        Paciente updated = pacienteRepository.save(existente);
        return pacienteMapper.toDto(updated);
    }

    public void deletePaciente(@NotNull Long id) {
        Paciente existente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado con id " + id));
        pacienteRepository.delete(existente);
    }
}

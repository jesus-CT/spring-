package com.example.demo.services;

import com.example.demo.dto.MedicoDTO;
import com.example.demo.mapper.MedicoMapper;
import com.example.demo.models.Medico;
import com.example.demo.models.Paciente;
import com.example.demo.repositories.MedicoRepository;
import com.example.demo.repositories.PacienteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoMapper medicoMapper;

    public MedicoService(MedicoRepository medicoRepository,
                         PacienteRepository pacienteRepository,
                         MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoMapper = medicoMapper;
    }

    public List<MedicoDTO> getAllMedicos() {
        return medicoRepository.findAll(Sort.by(Sort.Direction.ASC, "apellidos").and(Sort.by("nombre")))
                .stream()
                .map(medicoMapper::toDto)
                .collect(Collectors.toList());
    }

    public MedicoDTO getMedicoById(@NotNull Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id));
        return medicoMapper.toDto(medico);
    }

    public MedicoDTO createMedico(@NotNull @Valid MedicoDTO dto) {
        try {
            Medico medico = medicoMapper.toEntity(dto);
            medico.setId(null);

            // Guardo primero el médico para tener ID
            Medico savedMedico = medicoRepository.save(medico);

            // Asigno pacientes en el owning side (Paciente.medicos)
            if (dto.getPacienteIds() != null) {
                dto.getPacienteIds().forEach(pid -> {
                    Paciente p = pacienteRepository.findById(pid)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Paciente no encontrado con id " + pid));
                    p.getMedicos().add(savedMedico);
                    pacienteRepository.save(p);
                });
            }

            // 3. recargas el médico ya con su lista de pacientes (colección inversa)
            Medico reloaded = medicoRepository.findById(savedMedico.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "Error recargando médico"));

            // 4. mapeas y devuelves
            return medicoMapper.toDto(reloaded);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario '" + dto.getUsuario() + "' ya está en uso"
            );
        }
    }

    public MedicoDTO updateMedico(@NotNull Long id, @NotNull @Valid MedicoDTO dto) {
        Medico existente = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id));

        existente.setNombre(dto.getNombre());
        existente.setApellidos(dto.getApellidos());
        existente.setUsuario(dto.getUsuario());
        existente.setClave(dto.getClave());
        existente.setNumColegiado(dto.getNumColegiado());

        // Limpio las asociaciones previas en la tabla intermedia
        existente.getPacientes().forEach(p -> {
            p.getMedicos().remove(existente);
            pacienteRepository.save(p);
        });
        existente.getPacientes().clear();

        // Vuelvo a asignar según DTO
        if (dto.getPacienteIds() != null) {
            dto.getPacienteIds().forEach(pid -> {
                Paciente p = pacienteRepository.findById(pid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Paciente no encontrado con id " + pid));
                p.getMedicos().add(existente);
                pacienteRepository.save(p);
            });
        }

        Medico updated = medicoRepository.save(existente);
        return medicoMapper.toDto(updated);
    }

    public void deleteMedico(@NotNull Long id) {
        Medico existente = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id));
        medicoRepository.delete(existente);
    }
}

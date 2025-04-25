package com.example.demo.services;

import com.example.demo.dto.CitaDTO;
import com.example.demo.mapper.CitaMapper;
import com.example.demo.models.Cita;
import com.example.demo.models.Paciente;
import com.example.demo.models.Medico;
import com.example.demo.repositories.CitaRepository;
import com.example.demo.repositories.PacienteRepository;
import com.example.demo.repositories.MedicoRepository;
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
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final CitaMapper citaMapper;

    public CitaService(CitaRepository citaRepository,
                       PacienteRepository pacienteRepository,
                       MedicoRepository medicoRepository,
                       CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.citaMapper = citaMapper;
    }

    public List<CitaDTO> getAllCitas() {
        return citaRepository.findAll(Sort.by(Sort.Direction.ASC, "fechaHora"))
                .stream()
                .map(citaMapper::toDto)
                .collect(Collectors.toList());
    }

    public CitaDTO getCitaById(@NotNull Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id));
        return citaMapper.toDto(cita);
    }

    public CitaDTO createCita(@NotNull @Valid CitaDTO dto) {
        try {
            Cita cita = citaMapper.toEntity(dto);
            cita.setId(null);

            Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Paciente no encontrado con id " + dto.getPacienteId()));
            Medico medico = medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Medico no encontrado con id " + dto.getMedicoId()));

            cita.setPaciente(paciente);
            cita.setMedico(medico);

            Cita saved = citaRepository.save(cita);
            return citaMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Error de integridad al crear la cita"
            );
        }
    }

    public CitaDTO updateCita(@NotNull Long id, @NotNull @Valid CitaDTO dto) {
        Cita existente = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id));

        citaMapper.updateFromDto(dto, existente);

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Paciente no encontrado con id " + dto.getPacienteId()));
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Medico no encontrado con id " + dto.getMedicoId()));

        existente.setPaciente(paciente);
        existente.setMedico(medico);

        Cita updated = citaRepository.save(existente);
        return citaMapper.toDto(updated);
    }

    public void deleteCita(@NotNull Long id) {
        Cita existente = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id));
        citaRepository.delete(existente);
    }
}
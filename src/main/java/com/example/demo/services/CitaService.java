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

    // 1. Listar citas
    public List<CitaDTO> getAllCitas() {
        return citaRepository.findAll(Sort.by(Sort.Direction.ASC, "fechaHora"))
                .stream()
                .map(citaMapper::toDto)
                .collect(Collectors.toList());
    }

    // 2. Obtener una cita
    public CitaDTO getCitaById(@NotNull Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id));
        return citaMapper.toDto(cita);
    }

    // 3. Crear cita + diagnóstico anidado
    public CitaDTO createCita(@NotNull @Valid CitaDTO dto) {
        try {
            // El mapper ya monta cita.diagnostico
            Cita cita = citaMapper.toEntity(dto);
            cita.setId(null);

            // Asignar paciente y médico
            Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Paciente no encontrado con id " + dto.getPacienteId()));
            Medico medico = medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Médico no encontrado con id " + dto.getMedicoId()));

            cita.setPaciente(paciente);
            cita.setMedico(medico);

            // Se guarda cita y diagnóstico en cascada
            Cita saved = citaRepository.save(cita);
            return citaMapper.toDto(saved);

        } catch (DataIntegrityViolationException ex) {
            // Captura violaciones de la relación 1:1
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Error de integridad al crear la cita con diagnóstico"
            );
        }
    }

    // 4. Actualizar cita + diagnóstico anidado
    public CitaDTO updateCita(@NotNull Long id, @NotNull @Valid CitaDTO dto) {
        Cita existente = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id));

        // MapStruct actualiza fechaHora, motivo, attribute11 y diagnostico
        citaMapper.updateFromDto(dto, existente);

        // Volver a asignar paciente y médico
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Paciente no encontrado con id " + dto.getPacienteId()));
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Médico no encontrado con id " + dto.getMedicoId()));

        existente.setPaciente(paciente);
        existente.setMedico(medico);

        // Se guarda cita y diagnóstico (update) en cascada
        Cita updated = citaRepository.save(existente);
        return citaMapper.toDto(updated);
    }

    // 5. Borrar cita (el diagnóstico se elimina por orphanRemoval)
    public void deleteCita(@NotNull Long id) {
        Cita existente = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id));
        citaRepository.delete(existente);
    }
}

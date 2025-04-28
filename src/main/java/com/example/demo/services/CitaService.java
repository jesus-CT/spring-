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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

/**
 * Servicio genérico para Cita, con creación y actualización
 * especializadas para manejar Paciente, Médico y Diagnóstico en cascada.
 */
@Service
@Validated
public class CitaService
        extends AbstractService<CitaDTO, Cita, Long> {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final CitaMapper citaMapper;

    public CitaService(
            CitaRepository citaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            CitaMapper citaMapper
    ) {
        super(citaRepository, citaMapper, Cita.class);
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository   = medicoRepository;
        this.citaMapper         = citaMapper;
    }

    @Override
    public CitaDTO create(CitaDTO dto) {
        try {
            // Mapea DTO a entidad (incluye diagnóstico)
            Cita cita = citaMapper.toEntity(dto);
            cita.setId(null);

            // Recupera y asigna Paciente y Médico
            Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Paciente no encontrado con id " + dto.getPacienteId()
                    ));
            Medico medico = medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Médico no encontrado con id " + dto.getMedicoId()
                    ));
            cita.setPaciente(paciente);
            cita.setMedico(medico);

            // Guarda cita + diagnóstico en cascada
            Cita saved = repository.save(cita);
            return citaMapper.toDto(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Error de integridad al crear la cita con diagnóstico"
            );
        }
    }

    @Override
    public CitaDTO update(Long id, CitaDTO dto) {
        // Recupera la cita existente
        Cita existente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id
                ));

        // MapStruct actualiza fechaHora, motivoCita, attribute11 y diagnóstico
        citaMapper.updateEntityFromDto(dto, existente);

        // Reasigna Paciente y Médico
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Paciente no encontrado con id " + dto.getPacienteId()
                ));
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Médico no encontrado con id " + dto.getMedicoId()
                ));
        existente.setPaciente(paciente);
        existente.setMedico(medico);

        // Guarda actualización en cascada
        Cita updated = repository.save(existente);
        return citaMapper.toDto(updated);
    }


}

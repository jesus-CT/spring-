package com.example.demo.services;

import com.example.demo.dto.PacienteDTO;
import com.example.demo.mapper.PacienteMapper;
import com.example.demo.models.Paciente;
import com.example.demo.models.Medico;
import com.example.demo.repositories.PacienteRepository;
import com.example.demo.repositories.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class PacienteService
        extends AbstractService<PacienteDTO, Paciente, Long> {

    private final MedicoRepository medicoRepository;

    public PacienteService(
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            PacienteMapper pacienteMapper
    ) {
        super(pacienteRepository, pacienteMapper, Paciente.class);
        this.medicoRepository = medicoRepository;
    }

    @Override
    public PacienteDTO create(PacienteDTO dto) {
        // 1) Unicidad
        if (((PacienteRepository) repository).existsByNSS(dto.getNSS()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "NSS ya registrado");
        if (((PacienteRepository) repository).existsByNumTarjeta(dto.getNumTarjeta()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "NumTarjeta ya en uso");
        if (((PacienteRepository) repository).existsByUsuario(dto.getUsuario()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Usuario ya existe");

        // 2) Mapeo + asignación de Médicos
        Paciente paciente = mapper.toEntity(dto);
        paciente.setId(null);
        Set<Medico> medicos = dto.getMedicoIds() == null
                ? Collections.emptySet()
                : dto.getMedicoIds().stream()
                .map(mid -> medicoRepository.findById(mid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Médico no encontrado con id " + mid)))
                .collect(Collectors.toSet());
        paciente.setMedicos(medicos);

        // 3) Guardar y devolver DTO
        Paciente saved = repository.save(paciente);
        return mapper.toDto(saved);
    }

    @Override
    public PacienteDTO update(Long id, PacienteDTO dto) {
        Paciente existente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado con id " + id));

        // unicidad a la carta (ignorando este mismo id)
        if (((PacienteRepository) repository)
                .existsByNSSAndIdNot(dto.getNSS(), id))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "NSS registrado por otro paciente");
        if (((PacienteRepository) repository)
                .existsByNumTarjetaAndIdNot(dto.getNumTarjeta(), id))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "NumTarjeta en uso por otro paciente");
        if (((PacienteRepository) repository)
                .existsByUsuarioAndIdNot(dto.getUsuario(), id))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Usuario en uso por otro paciente");

        // MapStruct actualiza nombre, apellidos, usuario, clave, NSS, numTarjeta, telefono, direccion
        mapper.updateEntityFromDto(dto, existente);

        // reasignar méd​icos
        Set<Medico> medicos = dto.getMedicoIds() == null
                ? Collections.emptySet()
                : dto.getMedicoIds().stream()
                .map(mid -> medicoRepository.findById(mid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Médico no encontrado con id " + mid)))
                .collect(Collectors.toSet());
        existente.setMedicos(medicos);

        Paciente updated = repository.save(existente);
        return mapper.toDto(updated);
    }

    // delete(id) lo hereda de AbstractService y basta para eliminar el paciente
}

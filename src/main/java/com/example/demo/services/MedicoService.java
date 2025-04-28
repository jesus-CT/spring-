package com.example.demo.services;

import com.example.demo.dto.MedicoDTO;
import com.example.demo.mapper.MedicoMapper;
import com.example.demo.models.Medico;
import com.example.demo.models.Paciente;
import com.example.demo.repositories.MedicoRepository;
import com.example.demo.repositories.PacienteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class MedicoService
        extends AbstractService<MedicoDTO, Medico, Long> {

    private final PacienteRepository pacienteRepository;

    public MedicoService(
            MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository,
            MedicoMapper medicoMapper
    ) {
        super(medicoRepository, medicoMapper, Medico.class);
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public MedicoDTO create(MedicoDTO dto) {
        try {
            // 1) Mapeo inicial y guardamos para obtener ID
            Medico medico = mapper.toEntity(dto);
            medico.setId(null);
            Medico saved = repository.save(medico);

            // 2) Asignamos pacientes en el owning side (Paciente.medicos)
            if (dto.getPacienteIds() != null) {
                dto.getPacienteIds().forEach(pid -> {
                    Paciente p = pacienteRepository.findById(pid)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Paciente no encontrado con id " + pid
                            ));
                    p.getMedicos().add(saved);
                    pacienteRepository.save(p);
                });
            }

            // 3) Recargamos para traer la colección inversa y devolvemos DTO
            Medico reloaded = repository.findById(saved.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error recargando médico"
                    ));
            return mapper.toDto(reloaded);

        } catch (DataIntegrityViolationException ex) {
            // Captura violación de unique (usuario o numColegiado)
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario o numColegiado ya están en uso"
            );
        }
    }

    @Override
    public MedicoDTO update(Long id, MedicoDTO dto) {
        // 1) Recuperar existente
        Medico existente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id
                ));

        // 2) MapStruct actualiza campos básicos y numColegiado
        mapper.updateEntityFromDto(dto, existente);

        // 3) Limpiar asociaciones anteriores
        existente.getPacientes().forEach(p -> {
            p.getMedicos().remove(existente);
            pacienteRepository.save(p);
        });
        existente.getPacientes().clear();

        // 4) Reasignar según DTO
        if (dto.getPacienteIds() != null) {
            dto.getPacienteIds().forEach(pid -> {
                Paciente p = pacienteRepository.findById(pid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Paciente no encontrado con id " + pid
                        ));
                p.getMedicos().add(existente);
                pacienteRepository.save(p);
            });
        }

        // 5) Guardar y devolver DTO actualizado
        Medico updated = repository.save(existente);
        return mapper.toDto(updated);
    }

    // delete(id) queda heredado de AbstractService y basta para eliminar el médico
}

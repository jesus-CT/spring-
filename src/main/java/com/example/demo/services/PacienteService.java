package com.example.demo.services;

import com.example.demo.dto.PacienteDTO;
import com.example.demo.models.Paciente;
import com.example.demo.models.Medico;
import com.example.demo.repositories.PacienteRepository;
import com.example.demo.repositories.MedicoRepository;
import com.example.demo.mapper.PacienteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PacienteService extends AbstractService<PacienteDTO, Paciente, Long> implements IPacienteService {

    private final MedicoRepository medicoRepository;

    public PacienteService(
            PacienteRepository pacienteRepo,
            PacienteMapper mapper,
            MedicoRepository medicoRepository
    ) {
        super(pacienteRepo, mapper, Paciente.class);
        this.medicoRepository = medicoRepository;
    }

    @Override
    public PacienteDTO update(Long id, PacienteDTO dto) {
        Paciente paciente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado con id " + id
                ));

        // 1. Relación medicos
        Set<Medico> actuales = paciente.getMedicos();
        List<Long> idsDeseados = dto.getMedicoIds() != null ? dto.getMedicoIds() : List.of();
        Set<Medico> deseados = idsDeseados.stream()
                .map(mid -> medicoRepository.findById(mid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Medico no encontrado con id " + mid
                        )))
                .collect(Collectors.toSet());

        // 1.a Quitar los que ya no están
        actuales.stream()
                .filter(m -> !deseados.contains(m))
                .collect(Collectors.toSet())
                .forEach(paciente::removeMedico);
        // 1.b Añadir nuevos
        deseados.stream()
                .filter(m -> !actuales.contains(m))
                .forEach(paciente::addMedico);

        // 2. Campos básicos
        mapper.updateEntityFromDto(dto, paciente);

        // 3. Guardar y devolver
        Paciente saved = repository.save(paciente);
        return mapper.toDto(saved);
    }

    // Implementar assign/remove Medico si quieres, o delegar todo a PUT
}
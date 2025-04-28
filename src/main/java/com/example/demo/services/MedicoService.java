package com.example.demo.services;

import com.example.demo.dto.MedicoDTO;
import com.example.demo.mapper.MedicoMapper;
import com.example.demo.mapper.PacienteMapperHelper;
import com.example.demo.models.Medico;
import com.example.demo.models.Paciente;
import com.example.demo.repositories.MedicoRepository;
import com.example.demo.repositories.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicoService extends AbstractService<MedicoDTO, Medico, Long> implements IMedicoService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapperHelper pacienteMapperHelper;

    public MedicoService(
            MedicoRepository repository,
            MedicoMapper mapper,
            PacienteRepository pacienteRepository,
            PacienteMapperHelper pacienteMapperHelper
    ) {
        super(repository, mapper, Medico.class);
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapperHelper = pacienteMapperHelper;
    }

    @Override
    public MedicoDTO update(Long id, MedicoDTO dto) {
        // 1. Recuperar entidad existente
        Medico medico = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Medico no encontrado con id " + id
                ));

        // 2. Gestionar relaciones: pacientes
        Set<Paciente> actuales = medico.getPacientes();
        Set<Paciente> deseados = pacienteMapperHelper.mapIdsToPacientes(dto.getPacienteIds());

        // 2.a Eliminar pacientes no deseados
        actuales.stream()
                .filter(p -> !deseados.contains(p))
                .collect(Collectors.toSet()) // evita ConcurrentModification
                .forEach(medico::removePaciente);

        // 2.b Añadir pacientes nuevos
        deseados.stream()
                .filter(p -> !actuales.contains(p))
                .forEach(medico::addPaciente);

        // 3. Actualizar campos básicos (sin pacientes)
        // MapStruct ignora pacientes en updateEntityFromDto
        mapper.updateEntityFromDto(dto, medico);

        // 4. Guardar y devolver DTO
        Medico saved = repository.save(medico);
        return mapper.toDto(saved);
    }
}

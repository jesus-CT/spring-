package com.example.demo.services;

import com.example.demo.dto.DiagnosticoDTO;
import com.example.demo.mapper.DiagnosticoMapper;
import com.example.demo.models.Diagnostico;
import com.example.demo.repositories.CitaRepository;
import com.example.demo.repositories.DiagnosticoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

@Service
@Validated
public class DiagnosticoService
        extends AbstractService<DiagnosticoDTO, Diagnostico, Long> {

    private final CitaRepository citaRepository;

    public DiagnosticoService(
            DiagnosticoRepository repo,
            DiagnosticoMapper mapper,
            CitaRepository citaRepository
    ) {
        super(repo, mapper, Diagnostico.class);
        this.citaRepository = citaRepository;
    }

    @Override
    public DiagnosticoDTO create(DiagnosticoDTO dto) {
        throw new ResponseStatusException(
                HttpStatus.METHOD_NOT_ALLOWED,
                "La creación de Diagnóstico debe hacerse a través de CitaService"
        );
    }

    @Override
    public void delete(Long id) {
        Diagnostico existente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Diagnóstico no encontrado con id " + id
                ));
        citaRepository.delete(existente.getCita());
    }
}

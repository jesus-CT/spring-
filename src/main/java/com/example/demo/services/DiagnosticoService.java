package com.example.demo.services;

import com.example.demo.models.Diagnostico;
import com.example.demo.repositories.DiagnosticoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Service
@Validated
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;

    public DiagnosticoService(DiagnosticoRepository diagnosticoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
    }

    public List<Diagnostico> getAllDiagnosticos() {
        return diagnosticoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Diagnostico getDiagnosticoById(@NotNull Long id) {
        return diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Diagnóstico no encontrado con id " + id));
    }

    public Diagnostico createDiagnostico(@NotNull @Valid Diagnostico diagnostico) {
        diagnostico.setId(null);
        return diagnosticoRepository.save(diagnostico);
    }

    public Diagnostico updateDiagnostico(@NotNull Long id, @NotNull @Valid Diagnostico datosNuevos) {
        Diagnostico existente = getDiagnosticoById(id);
        existente.setValoracionEspecialista(datosNuevos.getValoracionEspecialista());
        existente.setEnfermedad(datosNuevos.getEnfermedad());
        return diagnosticoRepository.save(existente);
    }

    public void deleteDiagnostico(@NotNull Long id) {
        Diagnostico existente = getDiagnosticoById(id);
        diagnosticoRepository.delete(existente);
    }
}

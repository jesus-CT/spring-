package com.example.demo.services;

import com.example.demo.models.Medico;
import com.example.demo.repositories.MedicoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Validated
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll(Sort.by(Sort.Direction.ASC, "apellidos").and(Sort.by("nombre")));
    }

    public Medico getMedicoById(@NotNull Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id));
    }

    public Medico createMedico(@NotNull @Valid Medico medico) {
        medico.setId(null);
        return medicoRepository.save(medico);
    }

    public Medico updateMedico(@NotNull Long id, @NotNull @Valid Medico datosNuevos) {
        Medico existente = getMedicoById(id);
        existente.setNombre(datosNuevos.getNombre());
        existente.setApellidos(datosNuevos.getApellidos());
        existente.setUsuario(datosNuevos.getUsuario());
        existente.setClave(datosNuevos.getClave());
        existente.setNumColegiado(datosNuevos.getNumColegiado());
        return medicoRepository.save(existente);
    }

    public void deleteMedico(@NotNull Long id) {
        Medico existente = getMedicoById(id);
        medicoRepository.delete(existente);
    }
}

package com.example.demo.services;

import com.example.demo.models.Paciente;
import com.example.demo.repositories.PacienteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Validated
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll(Sort.by(Sort.Direction.ASC, "apellidos")
                .and(Sort.by("nombre")));
    }

    public Paciente getPacienteById(@NotNull Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado con id " + id));
    }

    public Paciente createPaciente(@NotNull @Valid Paciente paciente) {
        try {
            paciente.setId(null);
            return pacienteRepository.save(paciente);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario '" + paciente.getUsuario() + "' ya está en uso"
            );
        }
    }

    public Paciente updatePaciente(@NotNull Long id, @NotNull @Valid Paciente datosNuevos) {
        Paciente existente = getPacienteById(id);
        existente.setNombre(datosNuevos.getNombre());
        existente.setApellidos(datosNuevos.getApellidos());
        existente.setUsuario(datosNuevos.getUsuario());
        existente.setClave(datosNuevos.getClave());
        existente.setNSS(datosNuevos.getNSS());
        existente.setNumTarjeta(datosNuevos.getNumTarjeta());
        existente.setTelefono(datosNuevos.getTelefono());
        existente.setDireccion(datosNuevos.getDireccion());
        return pacienteRepository.save(existente);
    }

    public void deletePaciente(@NotNull Long id) {
        Paciente existente = getPacienteById(id);
        pacienteRepository.delete(existente);
    }
}

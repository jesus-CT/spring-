package com.example.demo.services;

import com.example.demo.models.Cita;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.repositories.CitaRepository;
import org.springframework.validation.annotation.Validated;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Cita> getAllCitas() {
        return citaRepository.findAll(Sort.by(Sort.Direction.ASC, "fechaHora"));
    }

    public Cita getCitaById(@NotNull Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con id " + id));
    }

    public Cita createCita(@NotNull @Valid Cita cita) {
        cita.setId(null);
        return citaRepository.save(cita);
    }

    public Cita updateCita(@NotNull Long id, @NotNull @Valid Cita datosNuevos) {
        Cita existente = getCitaById(id);
        existente.setFechaHora(datosNuevos.getFechaHora());
        existente.setMotivoCita(datosNuevos.getMotivoCita());
        existente.setAttribute11(datosNuevos.getAttribute11());

        return citaRepository.save(existente);
    }

    public void deleteCita(@NotNull Long id) {
        Cita existente = getCitaById(id);
        citaRepository.delete(existente);
    }
}

package com.example.demo.controllers;

import com.example.demo.models.Cita;
import com.example.demo.services.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<Cita>> getAllCitas() {
        return ResponseEntity.ok(citaService.getAllCitas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.getCitaById(id));
    }

    @PostMapping
    public ResponseEntity<Cita> createCita(@Valid @RequestBody Cita cita) {
        Cita creada = citaService.createCita(cita);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> updateCita(
            @PathVariable Long id,
            @Valid @RequestBody Cita datosNuevos) {

        Cita actualizada = citaService.updateCita(id, datosNuevos);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build();
    }
}

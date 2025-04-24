package com.example.demo.controllers;

import com.example.demo.models.Medico;
import com.example.demo.services.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public ResponseEntity<List<Medico>> getAll() {
        return ResponseEntity.ok(medicoService.getAllMedicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.getMedicoById(id));
    }

    @PostMapping
    public ResponseEntity<Medico> create(@Valid @RequestBody Medico medico) {
        Medico creado = medicoService.createMedico(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> update(
            @PathVariable Long id,
            @Valid @RequestBody Medico datosNuevos) {

        Medico actualizado = medicoService.updateMedico(id, datosNuevos);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicoService.deleteMedico(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.demo.controllers;

import com.example.demo.models.Diagnostico;
import com.example.demo.services.DiagnosticoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
@CrossOrigin(origins = "*")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(DiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    @GetMapping
    public ResponseEntity<List<Diagnostico>> getAll() {
        return ResponseEntity.ok(diagnosticoService.getAllDiagnosticos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diagnostico> getById(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosticoService.getDiagnosticoById(id));
    }

    @PostMapping
    public ResponseEntity<Diagnostico> create(@Valid @RequestBody Diagnostico diag) {
        Diagnostico creado = diagnosticoService.createDiagnostico(diag);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diagnostico> update(
            @PathVariable Long id,
            @Valid @RequestBody Diagnostico datosNuevos) {

        Diagnostico actualizado = diagnosticoService.updateDiagnostico(id, datosNuevos);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diagnosticoService.deleteDiagnostico(id);
        return ResponseEntity.noContent().build();
    }
}

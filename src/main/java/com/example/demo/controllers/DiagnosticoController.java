package com.example.demo.controllers;

import com.example.demo.dto.DiagnosticoDTO;
import com.example.demo.services.GenericService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;


import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
@CrossOrigin(origins = "*")
public class DiagnosticoController {

    private final GenericService<DiagnosticoDTO, Long> diagnosticoService;

    // Inyectamos la interfaz para que el proxy JDK encaje sin problemas
    public DiagnosticoController(
            @Qualifier ("diagnosticoService")
            GenericService<DiagnosticoDTO, Long> diagnosticoService
    ) {
        this.diagnosticoService = diagnosticoService;
    }

    @GetMapping
    public ResponseEntity<List<DiagnosticoDTO>> getAll() {
        return ResponseEntity.ok(diagnosticoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosticoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DiagnosticoDTO> create(
            @Valid @RequestBody DiagnosticoDTO dto
    ) {
        // El servicio devolverá 405 METHOD_NOT_ALLOWED si alguien intenta crear directamente
        DiagnosticoDTO creado = diagnosticoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DiagnosticoDTO dto
    ) {
        return ResponseEntity.ok(diagnosticoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diagnosticoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

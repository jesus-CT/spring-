package com.example.demo.controllers;

import com.example.demo.dto.DiagnosticoDTO;
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
    public ResponseEntity<List<DiagnosticoDTO>> getAll() {
        return ResponseEntity.ok(diagnosticoService.getAllDiagnosticos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosticoService.getDiagnosticoById(id));
    }

    @PostMapping
    public ResponseEntity<DiagnosticoDTO> create(@Valid @RequestBody DiagnosticoDTO dto) {
        DiagnosticoDTO creado = diagnosticoService.createDiagnostico(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DiagnosticoDTO dto) {
        DiagnosticoDTO actualizado = diagnosticoService.updateDiagnostico(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diagnosticoService.deleteDiagnostico(id);
        return ResponseEntity.noContent().build();
    }
}

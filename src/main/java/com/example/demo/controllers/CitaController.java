// src/main/java/com/example/demo/controllers/CitaController.java
package com.example.demo.controllers;

import com.example.demo.dto.CitaDTO;
import com.example.demo.services.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CitaDTO>> getAllCitas() {
        return ResponseEntity.ok(citaService.getAllCitas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> getCitaById(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.getCitaById(id));
    }

    @PostMapping
    public ResponseEntity<CitaDTO> createCita(@Valid @RequestBody CitaDTO dto) {
        CitaDTO creada = citaService.createCita(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> updateCita(
            @PathVariable Long id,
            @Valid @RequestBody CitaDTO dto) {
        CitaDTO actualizada = citaService.updateCita(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build();
    }
}
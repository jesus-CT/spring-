package com.example.demo.controllers;

import com.example.demo.dto.MedicoDTO;
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
    public ResponseEntity<List<MedicoDTO>> getAll() {
        return ResponseEntity.ok(medicoService.getAllMedicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.getMedicoById(id));
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> create(@Valid @RequestBody MedicoDTO medicoDto) {
        MedicoDTO created = medicoService.createMedico(medicoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicoDTO medicoDto) {
        MedicoDTO updated = medicoService.updateMedico(id, medicoDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicoService.deleteMedico(id);
        return ResponseEntity.noContent().build();
    }
}

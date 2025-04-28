package com.example.demo.controllers;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.services.GenericService;   // <— importa la interfaz
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final GenericService<UsuarioDTO, Long> usuarioService;

    public UsuarioController(
            @Qualifier("usuarioService")                       // opcional si sólo tienes un bean GenericService
            GenericService<UsuarioDTO, Long> usuarioService
    ) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

//    @PostMapping
//    public ResponseEntity<UsuarioDTO> create(
//            @Valid @RequestBody UsuarioDTO dto
//    ) {
//        UsuarioDTO creado = usuarioService.create(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto
    ) {
        return ResponseEntity.ok(usuarioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

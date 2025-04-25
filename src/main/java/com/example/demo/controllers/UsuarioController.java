package com.example.demo.controllers;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAll() {
        List<UsuarioDTO> dtos = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        UsuarioDTO dto = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(dto);
    }

//    @PostMapping
//    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO dto) {
//        UsuarioDTO creado = usuarioService.createUsuario(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto) {
        UsuarioDTO actualizado = usuarioService.updateUsuario(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}

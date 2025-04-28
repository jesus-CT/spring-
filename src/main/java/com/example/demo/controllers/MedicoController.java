package com.example.demo.controllers;

import com.example.demo.dto.MedicoDTO;
import com.example.demo.services.IMedicoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.StringReader;
import java.util.List;

/**
 * Controlador REST para Médicos.
 * Inyecta IMedicoService para desacoplar de la implementación.
 */
@RestController
@RequestMapping("/api/medicos")
@Validated
public class MedicoController {

    private final IMedicoService service;
    private final ObjectMapper objectMapper;

    public MedicoController(
            IMedicoService service,
            ObjectMapper objectMapper
    ) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<MedicoDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MedicoDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicoDTO create(@Valid @RequestBody MedicoDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public MedicoDTO update(
            @PathVariable Long id,
            @Valid @RequestBody MedicoDTO dto
    ) {
        return service.update(id, dto);
    }

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    public MedicoDTO patch(
            @PathVariable Long id,
            @RequestBody String patchJson
    ) {
        MedicoDTO original = service.findById(id);
        try (JsonReader reader = Json.createReader(new StringReader(
                objectMapper.writeValueAsString(original)))) {

            JsonStructure target = reader.read();
            JsonMergePatch mergePatch;
            try (JsonReader patchReader = Json.createReader(new StringReader(patchJson))) {
                mergePatch = Json.createMergePatch(patchReader.readValue());
            }

            JsonValue patched = mergePatch.apply(target);
            MedicoDTO dto = objectMapper.readValue(patched.toString(), MedicoDTO.class);
            return service.update(id, dto);

        } catch (JsonProcessingException | JsonException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Patch inválido o mal formado",
                    e
            );
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
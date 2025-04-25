package com.example.demo.services;

import com.example.demo.dto.MedicoDTO;
import com.example.demo.mapper.MedicoMapper;
import com.example.demo.models.Medico;
import com.example.demo.repositories.MedicoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    public List<MedicoDTO> getAllMedicos() {
        return medicoRepository
                .findAll(Sort.by(Sort.Direction.ASC, "apellidos").and(Sort.by("nombre")))
                .stream()
                .map(medicoMapper::toDto)
                .collect(Collectors.toList());
    }

    public MedicoDTO getMedicoById(@NotNull Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id));
        return medicoMapper.toDto(medico);
    }

    public MedicoDTO createMedico(@NotNull @Valid MedicoDTO medicoDto) {
        try {
            Medico medico = medicoMapper.toEntity(medicoDto);
            medico.setId(null);
            Medico saved = medicoRepository.save(medico);
            return medicoMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario '" + medicoDto.getUsuario() + "' ya está en uso"
            );
        }
    }

    public MedicoDTO updateMedico(@NotNull Long id, @NotNull @Valid MedicoDTO medicoDto) {
        if (!medicoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id);
        }
        try {
            Medico toUpdate = medicoMapper.toEntity(medicoDto);
            toUpdate.setId(id);
            Medico updated = medicoRepository.save(toUpdate);
            return medicoMapper.toDto(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario '" + medicoDto.getUsuario() + "' ya está en uso"
            );
        }
    }

    public void deleteMedico(@NotNull Long id) {
        Medico existing = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id " + id));
        medicoRepository.delete(existing);
    }
}

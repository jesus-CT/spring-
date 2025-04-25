package com.example.demo.mapper;

import org.mapstruct.Mapper;
import com.example.demo.models.Paciente;
import com.example.demo.dto.PacienteDTO;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
    PacienteDTO toDto(Paciente paciente);

    Paciente toEntity(PacienteDTO dto);
}
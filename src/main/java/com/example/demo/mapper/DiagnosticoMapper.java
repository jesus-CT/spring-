package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.demo.models.Diagnostico;
import com.example.demo.dto.DiagnosticoDTO;

@Mapper(componentModel = "spring")
public interface DiagnosticoMapper {

    @Mapping(source = "cita.id", target = "citaId")
    DiagnosticoDTO toDto(Diagnostico diagnostico);

    @Mapping(target = "cita", ignore = true)
    Diagnostico toEntity(DiagnosticoDTO dto);

    void updateFromDto(DiagnosticoDTO dto, @MappingTarget Diagnostico entidad);
}
package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.example.demo.models.Diagnostico;
import com.example.demo.dto.DiagnosticoDTO;

@Mapper(componentModel = "spring")
public interface DiagnosticoMapper {

    DiagnosticoDTO toDto(Diagnostico diagnostico);

    Diagnostico toEntity(DiagnosticoDTO dto);

    void updateFromDto(DiagnosticoDTO dto, @MappingTarget Diagnostico entidad);
}

package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.demo.dto.DiagnosticoDTO;
import com.example.demo.models.Diagnostico;

// Extiende la interfaz genérica para heredar toDtoList(...) y updateEntityFromDto(...)
@Mapper(componentModel = "spring")
public interface DiagnosticoMapper extends GenericMapper<DiagnosticoDTO, Diagnostico> {

    /** Mapea Diagnostico → DTO, extrayendo el id de la cita asociada */
    @Override
    @Mapping(source = "cita.id", target = "citaId")
    DiagnosticoDTO toDto(Diagnostico diagnostico);

    /** Mapea DTO → Diagnostico, ignorando el id (se genera en BD) */
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "citaId", target = "cita.id")
    Diagnostico toEntity(DiagnosticoDTO dto);

    /** Actualiza sólo los campos valoracionEspecialista y enfermedad */
    @Override
    void updateEntityFromDto(DiagnosticoDTO dto, @MappingTarget Diagnostico entidad);
}

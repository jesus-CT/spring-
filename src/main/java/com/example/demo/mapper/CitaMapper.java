package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.demo.models.Cita;
import com.example.demo.dto.CitaDTO;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "medico.id", target = "medicoId")
    CitaDTO toDto(Cita cita);

    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "medicoId", target = "medico.id")
    Cita toEntity(CitaDTO dto);

    void updateFromDto(CitaDTO dto, @MappingTarget Cita entidad);
}

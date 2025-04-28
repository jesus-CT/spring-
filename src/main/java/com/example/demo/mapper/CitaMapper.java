package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;
import com.example.demo.models.Cita;
import com.example.demo.dto.CitaDTO;

@Mapper(
        componentModel = "spring",
        uses = {DiagnosticoMapper.class}
)

public interface CitaMapper {

    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "medico.id", target = "medicoId")
    @Mapping(source = "diagnostico", target = "diagnostico")
    CitaDTO toDto(Cita cita);

    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "medicoId", target = "medico.id")
    @Mapping(source = "diagnostico", target = "diagnostico")
    Cita toEntity(CitaDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diagnostico.id", ignore = true)
    void updateFromDto(CitaDTO dto, @MappingTarget Cita entidad);

    @AfterMapping
    default void linkDiagnostico(@MappingTarget Cita cita) {
        if (cita.getDiagnostico() != null) {
            cita.getDiagnostico().setCita(cita);
        }
    }
}

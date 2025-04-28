package com.example.demo.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.demo.dto.CitaDTO;
import com.example.demo.models.Cita;

/**
 * Extiende GenericMapper para reutilizar toDtoList(...) y la firma estándar de updateEntityFromDto().
 * Usa DiagnosticoMapper para el mapeo anidado de diagnóstico.
 */
@Mapper(componentModel = "spring", uses = {DiagnosticoMapper.class})
public interface CitaMapper extends GenericMapper<CitaDTO, Cita> {

    /**
     * Entidad → DTO
     * - extrae paciente.id en pacienteId
     * - extrae medico.id en medicoId
     * - mapea anidado diagnóstico con DiagnosticoMapper
     */
    @Override
    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "medico.id",   target = "medicoId")
    @Mapping(source = "diagnostico", target = "diagnostico")
    CitaDTO toDto(Cita cita);

    /**
     * DTO → Entidad
     * - ignora el id (se genera en BD)
     * - asigna paciente.id y medico.id desde los campos DTO
     * - mapea anidado diagnóstico
     */
    @Override
    @Mapping(target = "id",           ignore = true)
    @Mapping(source = "pacienteId",   target = "paciente.id")
    @Mapping(source = "medicoId",     target = "medico.id")
    @Mapping(source = "diagnostico",  target = "diagnostico")
    Cita toEntity(CitaDTO dto);

    /**
     * Actualiza sólo fechaHora, motivoCita, attribute11 y el diagnóstico anidado.
     * Ignora el id y el id del diagnóstico existente.
     */
    @Override
    @Mapping(target = "id",                ignore = true)
    @Mapping(target = "diagnostico.id",    ignore = true)
    void updateEntityFromDto(CitaDTO dto, @MappingTarget Cita entidad);

    /**
     * Asegura que el Diagnóstico anidado tenga referencia de vuelta a la Cita padre
     * para que JPA gestione bien el cascade+orphanRemoval.
     */
    @AfterMapping
    default void linkDiagnostico(@MappingTarget Cita cita) {
        if (cita.getDiagnostico() != null) {
            cita.getDiagnostico().setCita(cita);
        }
    }
}

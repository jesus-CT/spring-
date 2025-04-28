package com.example.demo.mapper;

import org.mapstruct.*;
import com.example.demo.dto.MedicoDTO;
import com.example.demo.models.Medico;

@Mapper(componentModel = "spring", uses = { PacienteMapperHelper.class })
public interface MedicoMapper extends GenericMapper<MedicoDTO, Medico> {

    @Override
    @Mapping(source = "pacientes", target = "pacienteIds", qualifiedByName = "mapPacientesToIds")
    MedicoDTO toDto(Medico medico);

    @Override
    @Mapping(source = "pacienteIds", target = "pacientes", qualifiedByName = "mapIdsToPacientes")
    Medico toEntity(MedicoDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pacientes", ignore = true)
    void updateEntityFromDto(MedicoDTO dto, @MappingTarget Medico entidad);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "pacientes", ignore = true)
    void patchDtoToEntity(MedicoDTO dto, @MappingTarget Medico entidad);
}
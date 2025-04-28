package com.example.demo.mapper;

import com.example.demo.models.Medico;
import com.example.demo.models.Usuario;
import org.mapstruct.*;
import com.example.demo.dto.PacienteDTO;
import com.example.demo.models.Paciente;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PacienteMapper extends GenericMapper<PacienteDTO, Paciente> {

    @Override
    @Mapping(source = "medicos", target = "medicoIds", qualifiedByName = "mapMedicosToIds")
    PacienteDTO toDto(Paciente paciente);

    @Override
    @Mapping(source = "medicoIds", target = "medicos", qualifiedByName = "mapIdsToMedicos")
    Paciente toEntity(PacienteDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicos", ignore = true)
    void updateEntityFromDto(PacienteDTO dto, @MappingTarget Paciente entidad);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicos", ignore = true)
    void patchDtoToEntity(PacienteDTO dto, @MappingTarget Paciente entidad);

    /**
     * MapStruct usa estos métodos para mapear stub de IDs;
     * En el servicio los resolvemos a entidades reales.
     */
    @Named("mapIdsToMedicos")
    default Set<Medico> mapIdsToMedicos(List<Long> ids) {
        if (ids == null) return Collections.emptySet();
        return ids.stream()
                .map(id -> {
                    Medico m = new Medico();
                    m.setId(id);
                    return m;
                })
                .collect(Collectors.toSet());
    }

    @Named("mapMedicosToIds")
    default List<Long> mapMedicosToIds(Set<Medico> medicos) {
        if (medicos == null) return Collections.emptyList();
        return medicos.stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());
    }
}
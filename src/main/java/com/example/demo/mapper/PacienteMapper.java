package com.example.demo.mapper;

import com.example.demo.models.Medico;
import com.example.demo.models.Usuario;
import org.mapstruct.Mapper;
import com.example.demo.models.Paciente;
import com.example.demo.dto.PacienteDTO;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
    @Mapping(source = "medicos", target = "medicoIds")
    PacienteDTO toDto(Paciente paciente);

    @Mapping(target = "medicos", ignore = true)
    Paciente toEntity(PacienteDTO dto);

    @Mapping(target = "medicos", ignore = true)
    void updateFromDto(PacienteDTO dto, @MappingTarget Paciente entidad);

    // Métodos auxiliares para MapStruct:
    default List<Long> mapMedicosToIds(Set<Medico> medicos) {
        return medicos.stream().map(Usuario::getId).collect(Collectors.toList());
    }
    default Set<Medico> mapIdsToMedicos(List<Long> ids) {
        return ids == null ? null :
                ids.stream().map(id -> { Medico m = new Medico(); m.setId(id); return m; })
                        .collect(Collectors.toSet());
    }
}
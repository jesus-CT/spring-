package com.example.demo.mapper;

import com.example.demo.models.Paciente;
import com.example.demo.models.Usuario;
import org.mapstruct.Mapper;
import com.example.demo.models.Medico;
import com.example.demo.dto.MedicoDTO;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MedicoMapper {
    @Mapping(source = "pacientes", target = "pacienteIds")
    MedicoDTO toDto(Medico medico);

    @Mapping(target = "pacientes", ignore = true)
    Medico toEntity(MedicoDTO dto);

    @Mapping(target = "pacientes", ignore = true)
    void updateFromDto(MedicoDTO dto, @MappingTarget Medico entidad);

    default List<Long> mapPacientesToIds(Set<Paciente> pacientes) {
        return pacientes.stream().map(Usuario::getId).collect(Collectors.toList());
    }
    default Set<Paciente> mapIdsToPacientes(List<Long> ids) {
        return ids == null ? null :
                ids.stream().map(id -> { Paciente p = new Paciente(); p.setId(id); return p; })
                        .collect(Collectors.toSet());
    }
}

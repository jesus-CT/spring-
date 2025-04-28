package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.demo.dto.MedicoDTO;
import com.example.demo.models.Medico;
import com.example.demo.models.Paciente;
import com.example.demo.models.Usuario;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MedicoMapper extends GenericMapper<MedicoDTO, Medico> {

    /** Entidad → DTO: convierte Set<Paciente> a List<Long> de ids */
    @Override
    @Mapping(source = "pacientes", target = "pacienteIds")
    MedicoDTO toDto(Medico medico);

    /** DTO → entidad: ignora la colección, la gestionamos en el servicio */
    @Override
    @Mapping(target = "pacientes", ignore = true)
    Medico toEntity(MedicoDTO dto);

    /**
     * Actualización: no tocamos ni el id ni la colección,
     * MapStruct actualizará solo nombre, apellidos, usuario, clave y numColegiado.
     */
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pacientes", ignore = true)
    void updateEntityFromDto(MedicoDTO dto, @MappingTarget Medico entidad);

    /** Convierte la colección de Paciente a la lista de sus ids */
    default List<Long> mapPacientesToIds(Set<Paciente> pacientes) {
        if (pacientes == null) return null;
        return pacientes.stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());
    }

    /** Crea instancias de Paciente con solo id para enlazar la relación */
    default Set<Paciente> mapIdsToPacientes(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Paciente p = new Paciente();
                    p.setId(id);
                    return p;
                })
                .collect(Collectors.toSet());
    }
}

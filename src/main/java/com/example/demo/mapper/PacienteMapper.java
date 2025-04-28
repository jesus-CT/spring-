package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.demo.dto.PacienteDTO;
import com.example.demo.models.Paciente;
import com.example.demo.models.Medico;
import com.example.demo.models.Usuario;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PacienteMapper extends GenericMapper<PacienteDTO, Paciente> {

    /** Entidad → DTO: usa mapMedicosToIds para convertir Set<Medico> en List<Long> */
    @Override
    @Mapping(source = "medicos", target = "medicoIds")
    PacienteDTO toDto(Paciente paciente);

    /** DTO → Entidad: ignoramos collection, la gestionaremos en el servicio */
    @Override
    @Mapping(target = "medicos", ignore = true)
    Paciente toEntity(PacienteDTO dto);

    /** Actualiza sólo campos simples; la colección también la gestiona el servicio */
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicos", ignore = true)
    void updateEntityFromDto(PacienteDTO dto, @MappingTarget Paciente entidad);

    /**
     * Método de mapeo auxiliar que MapStruct detectará
     * para convertir tu Set<Medico> en List<Long> de IDs
     */
    default List<Long> mapMedicosToIds(Set<Medico> medicos) {
        if (medicos == null) {
            return null;
        }
        return medicos.stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());
    }

    /**
     * El inverso: convierte List<Long> en Set<Medico> con sólo el ID poblado,
     * para que JPA haga el bind correcto.
     */
    default Set<Medico> mapIdsToMedicos(List<Long> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream()
                .map(id -> {
                    Medico m = new Medico();
                    m.setId(id);
                    return m;
                })
                .collect(Collectors.toSet());
    }
}

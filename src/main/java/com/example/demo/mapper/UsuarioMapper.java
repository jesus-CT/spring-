package com.example.demo.mapper;

import com.example.demo.dto.MedicoDTO;
import com.example.demo.dto.PacienteDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.models.Medico;
import com.example.demo.models.Paciente;
import com.example.demo.models.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface UsuarioMapper extends GenericMapper<UsuarioDTO,Usuario> {

    @SubclassMapping(source = PacienteDTO.class,  target = Paciente.class)
    @SubclassMapping(source = MedicoDTO.class, target = Medico.class)
    @Override
    @Mapping(target = "id", ignore = true)
    Usuario toEntity(UsuarioDTO dto);
}

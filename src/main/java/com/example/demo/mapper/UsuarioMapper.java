package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.example.demo.models.Usuario;
import com.example.demo.dto.UsuarioDTO;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario usuario);

    // Eliminamos: Usuario toEntity(UsuarioDTO dto);

    /**
     * Actualiza una entidad existente a partir del DTO.
     * Como la entidad ya existe, no hay problema de clase abstracta.
     */
    void updateFromDto(UsuarioDTO dto, @MappingTarget Usuario entidad);

    List<UsuarioDTO> toDtoList(List<Usuario> usuarios);
}

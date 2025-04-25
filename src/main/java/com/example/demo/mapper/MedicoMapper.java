package com.example.demo.mapper;

import org.mapstruct.Mapper;
import com.example.demo.models.Medico;
import com.example.demo.dto.MedicoDTO;

@Mapper(componentModel = "spring")
public interface MedicoMapper {
    MedicoDTO toDto(Medico medico);

    Medico toEntity(MedicoDTO dto);
}

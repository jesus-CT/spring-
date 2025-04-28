package com.example.demo.mapper;

import org.mapstruct.MappingTarget;
import java.util.List;

public interface GenericMapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
    List<D> toDtoList(List<E> entities);


    void updateEntityFromDto(D dto, @MappingTarget E entity);
}

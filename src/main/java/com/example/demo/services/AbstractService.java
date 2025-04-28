package com.example.demo.services;

import com.example.demo.mapper.GenericMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

public abstract class AbstractService<D, E, ID> implements GenericService<D, ID> {

    protected final JpaRepository<E, ID> repository;
    protected final GenericMapper<D, E> mapper;
    private final Class<E> entityType;

    protected AbstractService(JpaRepository<E, ID> repository,
                              GenericMapper<D, E> mapper,
                              Class<E> entityType) {
        this.repository = repository;
        this.mapper     = mapper;
        this.entityType = entityType;
    }

    @Override
    public List<D> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public D findById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        entityType.getSimpleName() + " no encontrado con id " + id
                ));
        return mapper.toDto(entity);
    }

    @Override
    public D create(D dto) {
        E entity = mapper.toEntity(dto);
        E saved  = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public D update(ID id, D dto) {
        E existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        entityType.getSimpleName() + " no encontrado con id " + id
                ));
        mapper.updateEntityFromDto(dto, existing);
        E updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(ID id) {
        E existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        entityType.getSimpleName() + " no encontrado con id " + id
                ));
        repository.delete(existing);
    }
}

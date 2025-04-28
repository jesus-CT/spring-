package com.example.demo.services;

import com.example.demo.mapper.GenericMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
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
        try {
            E entity = mapper.toEntity(dto);
            // Aseguramos id nulo si existe
            resetId(entity);
            E saved = repository.save(entity);
            return mapper.toDto(
                    repository.findById(getId(saved))
                            .orElseThrow(this::serverError)
            );
        } catch (DataIntegrityViolationException ex) {
            throw conflictException(ex);
        }
    }

    @Override
    public D update(ID id, D dto) {
        try {
            E existing = repository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            entityType.getSimpleName() + " no encontrado con id " + id
                    ));
            mapper.updateEntityFromDto(dto, existing);
            E updated = repository.save(existing);
            repository.flush();    // <-- fuerza el SQL aquí, no al final de la transacción
            return mapper.toDto(updated);

        } catch (DataIntegrityViolationException ex) {
            throw conflictException(ex);
        }
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

    // ----------------------------- Helpers -----------------------------

    /**
     * Obtiene el ID genérico de la entidad vía reflection (invoca getId()).
     */
    protected ID getId(E entity) {
        try {
            @SuppressWarnings("unchecked")
            ID id = (ID) entity.getClass().getMethod("getId").invoke(entity);
            return id;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo obtener el ID de la entidad", e);
        }
    }

    /**
     * Pone a null la propiedad id de la entidad (invoca setId(null)).
     */
    protected void resetId(E entity) {
        try {
            for (Method m : entity.getClass().getMethods()) {
                if ("setId".equals(m.getName()) && m.getParameterCount() == 1) {
                    m.invoke(entity, new Object[]{null});
                    return;
                }
            }
            throw new NoSuchMethodException("Setter setId(id) no encontrado en " + entity.getClass());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo resetear el ID de la entidad", e);
        }
    }

    protected ResponseStatusException serverError() {
        return new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno procesando la entidad"
        );
    }

    protected ResponseStatusException conflictException(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();
        return new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Violación de integridad de datos: " + (cause != null ? cause.getMessage() : ex.getMessage())
        );
    }
}

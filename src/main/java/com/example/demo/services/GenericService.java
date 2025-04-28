package com.example.demo.services;

import java.util.List;

public interface GenericService<D, ID> {
    List<D> findAll();
    D findById(ID id);
    D create(D dto);
    D update(ID id, D dto);
    void delete(ID id);
}

package com.epam.service.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T> {
    List<T> findAll();
    Optional<T> findById(long id);
    T save(T entity);
    void delete(long id);
}
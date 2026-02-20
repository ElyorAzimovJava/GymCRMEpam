package com.epam.service.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseDAO<T extends Serializable> {
    List<T> findAll();

    T findById(long id);

    T save(T entity);

    void delete(long id);

    T update(T entity);
}
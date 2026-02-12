package com.epam.service.dao;

import com.epam.service.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAO implements BaseDAO<Trainer> {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);

    private final Storage storage;

    @Autowired
    public TrainerDAO(Storage storage) {
        this.storage = storage;
    }

    @Override
    public List<Trainer> findAll() {
        logger.debug("Finding all trainers");
        return new ArrayList<>(storage.getTrainerStore().values());
    }

    @Override
    public Optional<Trainer> findById(long id) {
        logger.debug("Finding trainer by id: {}", id);
        return Optional.ofNullable(storage.getTrainerStore().get(id));
    }

    @Override
    public Trainer save(Trainer entity) {
        logger.debug("Saving trainer with id: {}", entity.getId());
        storage.getTrainerStore().put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        logger.debug("Deleting trainer with id: {}", id);
        storage.getTrainerStore().remove(id);
    }
}
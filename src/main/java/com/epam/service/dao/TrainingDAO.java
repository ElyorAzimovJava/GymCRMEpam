package com.epam.service.dao;

import com.epam.service.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDAO implements BaseDAO<Training> {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);

    private final Storage storage;

    @Autowired
    public TrainingDAO(Storage storage) {
        this.storage = storage;
    }

    @Override
    public List<Training> findAll() {
        logger.debug("Finding all trainings");
        return new ArrayList<>(storage.getTrainingStore().values());
    }

    @Override
    public Optional<Training> findById(long id) {
        logger.debug("Finding training by id: {}", id);
        return Optional.ofNullable(storage.getTrainingStore().get(id));
    }

    @Override
    public Training save(Training entity) {
        logger.debug("Saving training with id: {}", entity.getId());
        storage.getTrainingStore().put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        logger.debug("Deleting training with id: {}", id);
        storage.getTrainingStore().remove(id);
    }
}
package com.epam.service.dao;

import com.epam.service.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDAO implements BaseDAO<Trainee> {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    private final Storage storage;

    @Autowired
    public TraineeDAO(Storage storage) {
        this.storage = storage;
    }

    @Override
    public List<Trainee> findAll() {
        logger.debug("Finding all trainees");
        return new ArrayList<>(storage.getTraineeStore().values());
    }

    @Override
    public Optional<Trainee> findById(long id) {
        logger.debug("Finding trainee by id: {}", id);
        return Optional.ofNullable(storage.getTraineeStore().get(id));
    }

    @Override
    public Trainee save(Trainee entity) {
        logger.debug("Saving trainee with id: {}", entity.getId());
        storage.getTraineeStore().put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        logger.debug("Deleting trainee with id: {}", id);
        storage.getTraineeStore().remove(id);
    }
}
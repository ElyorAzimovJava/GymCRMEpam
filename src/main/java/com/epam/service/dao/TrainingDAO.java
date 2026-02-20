package com.epam.service.dao;

import com.epam.service.model.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingDAO implements BaseDAO<Training> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Training> findAll() {
        return getCurrentSession().createQuery("from Training").list();
    }

    @Override
    public Training findById(long id) {
        return getCurrentSession().get(Training.class, id);
    }

    @Override
    public Training save(Training entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        Training training = findById(id);
        getCurrentSession().delete(training);
    }

    @Override
    public Training update(Training entity) {
        return (Training) getCurrentSession().merge(entity);
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
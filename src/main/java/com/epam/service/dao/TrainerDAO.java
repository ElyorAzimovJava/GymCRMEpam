package com.epam.service.dao;

import com.epam.service.model.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerDAO implements BaseDAO<Trainer> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Trainer> findAll() {
        return getCurrentSession().createQuery("from Trainer").list();
    }

    @Override
    public Trainer findById(long id) {
        return getCurrentSession().get(Trainer.class, id);
    }

    @Override
    public Trainer save(Trainer entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        Trainer trainer = findById(id);
        getCurrentSession().delete(trainer);
    }

    @Override
    public Trainer update(Trainer entity) {
        return (Trainer) getCurrentSession().merge(entity);
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
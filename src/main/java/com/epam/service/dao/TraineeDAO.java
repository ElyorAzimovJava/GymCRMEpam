package com.epam.service.dao;

import com.epam.service.model.Trainee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO:
//  Implementation works correctly, but this DAO approach is considered low-level
//  compared to modern Spring persistence practices.
//  In most real projects repository layers are implemented using Spring Data JPA,
//  which eliminates boilerplate and provides built-in CRUD.
//  @Repository
//  public interface TraineeRepository extends JpaRepository<Trainee, Long> {}
//  2 lines above remove 95% boilerplate
@Repository
public class TraineeDAO implements BaseDAO<Trainee> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Trainee> findAll() {
        return getCurrentSession().createQuery("from Trainee").list();
    }

    @Override
    public Trainee findById(long id) {
        return getCurrentSession().get(Trainee.class, id);
    }

    @Override
    public Trainee save(Trainee entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        Trainee trainee = findById(id);
        getCurrentSession().delete(trainee);
    }

    @Override
    public Trainee update(Trainee entity) {
        return (Trainee) getCurrentSession().merge(entity);
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
package com.example.phanquyen.repository.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class Hibernate {

    @PersistenceContext // = @Autowired
    EntityManager entityManager;


    //  ========= Read ==========
    public <T> List<T> getObjectsHibernateQuery(Class<T> resultType, String query){
        return entityManager.createQuery(query, resultType).getResultList();
    }

    public <T> List<T> getObjectsNativeQuery(Class<T> resultType, String query){
        return entityManager.createNativeQuery(query, resultType).getResultList();
    }

    public <T> T getByID(Class<T> type, Long ID){
        return entityManager.find(type, ID);
    }

    @Transactional
    public <T> void save(T e){
        entityManager.persist(e);
    }

    @Transactional
    public <T> T update(T e){
        return entityManager.merge(e);
    }

    @Transactional
    public <T> void delete(int id, Class<T> resultType){
        T e = entityManager.find(resultType, id);
        entityManager.remove(e);
    }
}

package org.homs.lechuga.repository;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.Order;
import org.homs.lechuga.entity.query.QueryProcessor;
import org.homs.lentejajdbc.exception.EmptyResultException;

import java.util.Optional;

public class LechugaRepository<E, ID> {

    private final EntityManager<E, ID> entityManager;

    public LechugaRepository(EntityManager<E, ID> entityManager) {
        this.entityManager = entityManager;
    }

    public void insert(E entity) {
        entityManager.insert(entity);
    }

    public void update(E entity) {
        entityManager.update(entity);
    }

    public void save(E entity) {
        entityManager.store(entity);
    }

    public void saveAll(Iterable<E> entities) {
        entityManager.storeAll(entities);
    }

    public Optional<E> findById(ID id) {
        try {
            return Optional.of(entityManager.loadById(id));
        } catch (EmptyResultException e) {
            return Optional.empty();
        }
    }

    public boolean existsById(ID id) {
        return entityManager.existsById(id);
    }

    public Iterable<E> findAll(Order... orders) {
        return entityManager.loadAll(orders);
    }

    public Iterable<E> findByProperty(String propertyName, Object propertyValue, Order... orders) {
        return entityManager.loadByProperty(propertyName, propertyValue, orders);
    }

    public Iterable<E> findAllById(Iterable<ID> ids) {
        return entityManager.loadByIds(ids);
    }

    public void deleteById(ID id) {
        entityManager.deleteById(id);
    }

    public void delete(E entity) {
        entityManager.delete(entity);
    }

    public QueryProcessor<E> createQuery(String selfAlias) {
        return entityManager.createQuery(selfAlias);
    }

    public EntityManager<E, ID> getEntityManager() {
        return entityManager;
    }
}
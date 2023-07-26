package org.homs.lechuga.entity;

import org.homs.lentejajdbc.DataAccesFacade;

public class EntityManagerBuilder {

    final DataAccesFacade facade;
    final EntityModelBuilder entityModelBuilder;

    public EntityManagerBuilder(DataAccesFacade facade) {
        this.facade = facade;
        this.entityModelBuilder = new EntityModelBuilder();
    }

    public <E, ID> EntityManager<E, ID> build(EntityModel<E> entityModel) {
        return new EntityManager<>(entityModel, facade);
    }

    public <E, ID> EntityManager<E, ID> build(Class<E> entityClass) {
        return new EntityManager<>(entityModelBuilder.build(entityClass), facade);
    }
}

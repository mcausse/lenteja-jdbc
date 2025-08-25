package org.homs.lechuga.entity;

import org.homs.lentejajdbc.IJdbcFacade;

public class EntityManagerBuilder {

    final IJdbcFacade facade;
    final EntityModelBuilder entityModelBuilder;

    public EntityManagerBuilder(IJdbcFacade facade) {
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

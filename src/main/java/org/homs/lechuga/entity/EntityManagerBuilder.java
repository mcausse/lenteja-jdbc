package org.homs.lechuga.entity;

import org.homs.lechuga.conventions.Conventions;
import org.homs.lechuga.conventions.DefaultConventions;
import org.homs.lechuga.entity.anno.Embedded;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.anno.Transient;
import org.homs.lechuga.entity.reflect.BeanProperty;
import org.homs.lentejajdbc.DataAccesFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EntityManagerBuilder {

    final Conventions conventions;

    public EntityManagerBuilder(Conventions conventions) {
        this.conventions = conventions;
    }

    public EntityManagerBuilder() {
        this(new DefaultConventions());
    }

    public <E, ID> EntityManager<E, ID> buildEntityManager(DataAccesFacade facade, Class<E> entityClass) {
        return new EntityManager<>(buildEntityModel(entityClass), facade);
    }

    public <E> EntityModel<E> buildEntityModel(Class<E> entityClass) {
        final String tableName;
        if (entityClass.isAnnotationPresent(Table.class) && !entityClass.getAnnotation(Table.class).value().isEmpty()) {
            tableName = entityClass.getAnnotation(Table.class).value();
        } else {
            tableName = conventions.tableNameOf(entityClass);
        }
        return new EntityModel<>(
                entityClass,
                tableName,
                buildPropertiesForEntityClass(entityClass)
        );
    }

    protected List<EntityPropertyModel> buildPropertiesForEntityClass(Class<?> entityClass) {

        Stack<BeanProperty> propertyNamesStack = new Stack<>();
        var properties = BeanProperty.findBeanProperties(entityClass);
        return buildPropertiesForEntityClass(entityClass, propertyNamesStack, properties);
    }

    protected List<EntityPropertyModel> buildPropertiesForEntityClass(Class<?> rootEntityClass, Stack<BeanProperty> propertyNamesStack, List<BeanProperty> properties) {
        List<EntityPropertyModel> entityProperties = new ArrayList<>();

        for (var property : properties) {

            if (property.hasAnnotation(Transient.class)) {
                continue;
            }

            propertyNamesStack.push(property);
            if (property.hasAnnotation(Embedded.class)) {
                var childrenProperties = BeanProperty.findBeanProperties(property.getType());
                entityProperties.addAll(buildPropertiesForEntityClass(rootEntityClass, propertyNamesStack, childrenProperties));
            } else {
                entityProperties.add(new EntityPropertyModel(rootEntityClass, new ArrayList<>(propertyNamesStack), conventions));
            }
            propertyNamesStack.pop();
        }

        return entityProperties;
    }
}

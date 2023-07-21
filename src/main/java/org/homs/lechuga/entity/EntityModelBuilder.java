package org.homs.lechuga.entity;

import org.homs.lechuga.conventions.Conventions;
import org.homs.lechuga.conventions.DefaultConventions;
import org.homs.lechuga.entity.anno.Embedded;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.anno.Transient;
import org.homs.lechuga.entity.reflect.BeanProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class EntityModelBuilder {

    final Conventions conventions;

    public EntityModelBuilder(Conventions conventions) {
        this.conventions = conventions;
    }

    public EntityModelBuilder() {
        this(new DefaultConventions());
    }

    public <E> EntityModel<E> build(Class<E> entityClass) {
        final String tableName;
        if (entityClass.isAnnotationPresent(Table.class) && !entityClass.getAnnotation(Table.class).value().isEmpty()) {
            tableName = entityClass.getAnnotation(Table.class).value();
        } else {
            tableName = conventions.tableNameOf(entityClass);
        }

        List<BeanProperty> ids = BeanProperty.findBeanProperties(entityClass, bp -> bp.hasAnnotation(Id.class));
        if (ids.isEmpty()) {
            throw new RuntimeException("a single @" + Id.class.getName() + "-annotated field is mandatory; in entity '" + entityClass.getName() + "'");
        }
        if (ids.size() > 1) {
            throw new RuntimeException("more than 1 @" + Id.class.getName() + "-annotated field; in entity '" + entityClass.getName() + "'");
        }
        BeanProperty id = ids.get(0);
        List<BeanProperty> nonIds = BeanProperty.findBeanProperties(entityClass, bp -> !bp.hasAnnotation(Id.class));


        List<EntityPropertyModel> idProperties = buildPropertyModel(entityClass, new Stack<>(), id);

        List<EntityPropertyModel> nonIdProperties = new ArrayList<>();
        for (var nonId : nonIds) {
            nonIdProperties.addAll(buildPropertyModel(entityClass, new Stack<>(), nonId));
        }

        return new EntityModel<>(
                entityClass,
                tableName,
                idProperties,
                nonIdProperties
        );
    }

    private List<EntityPropertyModel> buildPropertyModel(Class<?> rootEntityClass, Stack<BeanProperty> propertyNamesStack, BeanProperty property) {

        if (property.hasAnnotation(Transient.class)) {
            return Collections.emptyList();
        }

        List<EntityPropertyModel> entityProperties = new ArrayList<>();
        propertyNamesStack.push(property);
        if (property.hasAnnotation(Embedded.class)) {
            var childrenProperties = BeanProperty.findBeanProperties(property.getType(), null);
            for (var childProp : childrenProperties) {
                entityProperties.addAll(buildPropertyModel(rootEntityClass, propertyNamesStack, childProp));
            }
        } else {
            entityProperties.add(new EntityPropertyModel(rootEntityClass, new ArrayList<>(propertyNamesStack), conventions));
        }
        propertyNamesStack.pop();

        return entityProperties;
    }
}

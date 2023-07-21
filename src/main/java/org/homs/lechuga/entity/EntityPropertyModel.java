package org.homs.lechuga.entity;

import org.homs.lechuga.conventions.Conventions;
import org.homs.lechuga.entity.anno.*;
import org.homs.lechuga.entity.generator.Generator;
import org.homs.lechuga.entity.handlers.ColumnHandler;
import org.homs.lechuga.entity.handlers.impl.EnumColumnHandler;
import org.homs.lechuga.entity.handlers.impl.Handlers;
import org.homs.lechuga.entity.reflect.BeanProperty;
import org.homs.lechuga.entity.reflect.ReflectUtils;

import java.util.List;

public class EntityPropertyModel {

    final Class<?> entityClass;
    final String propertiesPath;
    final List<BeanProperty> beanPropertiesPath;

    final boolean isId;
    final Generator generator;
    final ColumnHandler columnHandler;
    final String columnName;

    public EntityPropertyModel(Class<?> entityClass, List<BeanProperty> beanPropertiesPath, Conventions conventions) {
        this.entityClass = entityClass;
        this.propertiesPath = concatPath(beanPropertiesPath);
        this.beanPropertiesPath = beanPropertiesPath;

        this.isId = isId(beanPropertiesPath);
        this.generator = getGenerator(beanPropertiesPath);
        this.columnHandler = getColumnHandler(beanPropertiesPath);
        this.columnName = getColumnName(beanPropertiesPath, conventions);
    }

    protected String concatPath(List<BeanProperty> beanPropertiesPath) {
        var strb = new StringBuilder();
        for (int i = 0; i < beanPropertiesPath.size(); i++) {
            var beanProperty = beanPropertiesPath.get(i);
            if (i > 0) {
                strb.append('.');
            }
            strb.append(beanProperty.getName());
        }
        return strb.toString();
    }

    protected boolean isId(List<BeanProperty> beanPropertiesPath) {
        return beanPropertiesPath.stream().anyMatch(p -> p.hasAnnotation(Id.class));
    }

    protected Generator getGenerator(List<BeanProperty> beanPropertiesPath) {
        var lastProperty = beanPropertiesPath.get(beanPropertiesPath.size() - 1);
        if (lastProperty.hasAnnotation(Generated.class)) {
            Generated annoGenerated = lastProperty.getAnnotation(Generated.class);
            return ReflectUtils.newInstance(annoGenerated.value(), annoGenerated.args());
        }
        return null;
    }

    protected ColumnHandler getColumnHandler(List<BeanProperty> beanPropertiesPath) {
        var lastProperty = beanPropertiesPath.get(beanPropertiesPath.size() - 1);

        if (lastProperty.hasAnnotation(Enumerated.class)) {
            if (!Enum.class.isAssignableFrom(lastProperty.getType())) {
                throw new RuntimeException("@" + Enumerated.class.getName() + " present in property '" + lastProperty + "' but is of type " + lastProperty.getType().getName());
            }
            return new EnumColumnHandler((Class<? extends Enum>) lastProperty.getType());
        }

        if (lastProperty.hasAnnotation(Handler.class)) {
            Handler annoGenerated = lastProperty.getAnnotation(Handler.class);
            return ReflectUtils.newInstance(annoGenerated.value(), annoGenerated.args());
        }
        return Handlers.getHandlerFor(lastProperty.getType());
    }

    protected String getColumnName(List<BeanProperty> beanPropertiesPath, Conventions conventions) {
        var lastProperty = beanPropertiesPath.get(beanPropertiesPath.size() - 1);
        if (lastProperty.hasAnnotation(Column.class)) {
            Column annoColumn = lastProperty.getAnnotation(Column.class);
            return annoColumn.value();
        } else {
            return conventions.columnNameOf(lastProperty.getName());
        }
    }

    public void setValue(Object entity, Object value) {
        Object o = entity;
        int i;
        for (i = 0; i < this.beanPropertiesPath.size() - 1; i++) {
            BeanProperty property = this.beanPropertiesPath.get(i);
            var propValue = property.getValue(o);
            if (propValue == null) {
                var newValue = ReflectUtils.newInstance(property.getType());
                property.setValue(o, newValue);
                propValue = newValue;
            }
            o = propValue;
        }
        this.beanPropertiesPath.get(i).setValue(o, value);
    }

    public Object getValue(Object entity) {
        return getValue(entity, 0);
    }

    public Object getValueFromId(Object id) {
        return getValue(id, 1);
    }

    public Object getValue(Object entity, int propertySkipLevel) {

        // covers the propertySkipLevel>0 with scalar ids
        if (propertySkipLevel >= this.beanPropertiesPath.size()) {
            return entity;
        }

        Object o = entity;
        int i;
        for (i = propertySkipLevel; i < this.beanPropertiesPath.size() - 1; i++) {
            BeanProperty property = this.beanPropertiesPath.get(i);
            var propValue = property.getValue(o);
            if (propValue == null) {
                return null;
            }
            o = propValue;
        }
        return this.beanPropertiesPath.get(i).getValue(o);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getPropertiesPath() {
        return propertiesPath;
    }

    public boolean isId() {
        return isId;
    }

    public Generator getGenerator() {
        return generator;
    }

    public ColumnHandler getColumnHandler() {
        return columnHandler;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public String toString() {
        return entityClass.getName() + "#" + propertiesPath;
    }

//    public <E> Object getValueForJdbc(E entity) {
//        return columnHandler.getJdbcValue(getValue(entity));
//    }

    public Object convertValueForJdbc(Object value) {
        return columnHandler.getJdbcValue(value);
    }

    public Class<?> getPropertyType() {
        var lastProperty = beanPropertiesPath.get(beanPropertiesPath.size() - 1);
        return lastProperty.getType();
    }
}

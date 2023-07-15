package org.homs.lechuga.conventions;

public interface Conventions {

    String tableNameOf(Class<?> entityClass);

    String columnNameOf(String propertyName);

}
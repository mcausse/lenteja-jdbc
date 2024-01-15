package org.homs.lechuga.entity.handlers.impl;

import org.homs.lechuga.entity.handlers.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumColumnHandler implements ColumnHandler {

    final Class<? extends Enum> enumClass;

    public EnumColumnHandler(Class<? extends Enum> enumClass) {
        super();
        this.enumClass = enumClass;
    }

    @Override
    public Object getJdbcValue(Object value) {
        if (value == null) {
            return null;
        }
        return ((Enum<?>) value).name();
    }

    @Override
    public Object readValue(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        if (name == null) {
            return null;
        }
        return Enum.valueOf(enumClass, name);
    }
}
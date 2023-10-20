package org.homs.lechuga.entity.handlers.impl;

import org.homs.lechuga.entity.handlers.ColumnHandler;
import org.homs.lechuga.exception.LechugaException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassStringHandler implements ColumnHandler {

    @Override
    public Object getJdbcValue(Object value) {
        if (value == null) {
            return null;
        }
        return value.getClass().getName();
    }

    @Override
    public Object readValue(ResultSet rs, String columnName) throws SQLException {
        String v = rs.getString(columnName);
        if (v == null) {
            return null;
        }
        try {
            return Class.forName(v);
        } catch (Exception e) {
            throw new LechugaException("for class: " + v, e);
        }
    }

}
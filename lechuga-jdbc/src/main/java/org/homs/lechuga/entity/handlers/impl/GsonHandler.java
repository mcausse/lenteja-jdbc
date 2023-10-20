//package org.homs.lechuga.entity.handlers.impl;
//
//import com.google.gson.Gson;
//import org.homs.lechuga.entity.handlers.ColumnHandler;
//import org.homs.lechuga.exception.LechugaException;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class GsonHandler implements ColumnHandler {
//
//    final Class<?> jsonClass;
//
//    public GsonHandler(Class<?> jsonClass) {
//        this.jsonClass = jsonClass;
//    }
//
//    @Override
//    public Object getJdbcValue(Object value) {
//        if (value == null) {
//            return null;
//        }
//        if (!jsonClass.isAssignableFrom(value.getClass())) {
//            throw new RuntimeException(); // TODO
//        }
//        return new Gson().toJson(value);
//    }
//
//    @Override
//    public Object readValue(ResultSet rs, String columnName) throws SQLException {
//        String v = rs.getString(columnName);
//        if (v == null) {
//            return null;
//        }
//        try {
//            return new Gson().fromJson(v, jsonClass);
//        } catch (Exception e) {
//            throw new LechugaException("for class: " + v, e);
//        }
//    }
//
//}
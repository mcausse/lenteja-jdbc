package org.homs.lechuga.entity.handlers.impl;

import org.homs.lechuga.entity.handlers.ColumnHandler;
import org.homs.lechuga.exception.LechugaException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateStringHandler implements ColumnHandler {

    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    final String dateFormat;

    public DateStringHandler(String dateFormat) {
        super();
        this.dateFormat = dateFormat;
    }

    public DateStringHandler() {
        this(DEFAULT_DATE_FORMAT);
    }

    @Override
    public Object getJdbcValue(Object value) {
        if (value == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(value);
    }

    @Override
    public Object readValue(ResultSet rs, String columnName) throws SQLException {
        String v = rs.getString(columnName);
        if (v == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(v);
        } catch (ParseException e) {
            throw new LechugaException("parsing '" + v + "' with format: '" + dateFormat + "'", e);
        }
    }

}
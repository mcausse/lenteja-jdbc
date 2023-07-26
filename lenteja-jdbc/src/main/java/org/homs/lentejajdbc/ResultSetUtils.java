package org.homs.lentejajdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultSetUtils {

    public static Map<String, Object> extractRowAsMap(ResultSet rs) throws SQLException {
        Map<String, Object> r = new LinkedHashMap<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            var columnName = rs.getMetaData().getColumnName(i);
            var columnValue = rs.getObject(columnName);
            r.put(columnName, columnValue);
        }
        return r;
    }

    public static Byte getByte(final ResultSet rs) throws SQLException {
        final byte v = rs.getByte(1);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Short getShort(final ResultSet rs) throws SQLException {
        final short v = rs.getShort(1);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Integer getInteger(final ResultSet rs) throws SQLException {
        final int v = rs.getInt(1);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Long getLong(final ResultSet rs) throws SQLException {
        final long v = rs.getLong(1);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Float getFloat(final ResultSet rs) throws SQLException {
        final float v = rs.getFloat(1);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Double getDouble(final ResultSet rs) throws SQLException {
        final double v = rs.getDouble(1);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static String getString(final ResultSet rs) throws SQLException {
        return rs.getString(1);
    }

    public static Boolean getBoolean(final ResultSet rs) throws SQLException {
        boolean v = rs.getBoolean(1);
        //		A method that returns either Boolean.TRUE, Boolean.FALSE or null is an accident waiting to happen.
        //		This method can be invoked as though it returned a value of type boolean, andthe compiler will insert
        //		automatic unboxing of the Boolean value. If a null value is returned,this will result in a NullPointerException.
        //
        //		if (rs.wasNull()) {
        //			return null;
        //		}
        return v;
    }

    public static byte[] getBytes(final ResultSet rs) throws SQLException {
        return rs.getBytes(1);
    }

    public static Timestamp getTimestamp(final ResultSet rs) throws SQLException {
        return rs.getTimestamp(1);
    }

    public static Byte getByte(final ResultSet rs, final String columnLabel) throws SQLException {
        final byte v = rs.getByte(columnLabel);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Short getShort(final ResultSet rs, final String columnLabel) throws SQLException {
        final short v = rs.getShort(columnLabel);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Integer getInteger(final ResultSet rs, final String columnLabel) throws SQLException {
        final int v = rs.getInt(columnLabel);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Long getLong(final ResultSet rs, final String columnLabel) throws SQLException {
        final long v = rs.getLong(columnLabel);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Float getFloat(final ResultSet rs, final String columnLabel) throws SQLException {
        final float v = rs.getFloat(columnLabel);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Double getDouble(final ResultSet rs, final String columnLabel) throws SQLException {
        final double v = rs.getDouble(columnLabel);
        if (rs.wasNull()) {
            return null;
        }
        return v;
    }

    public static Boolean getBoolean(final ResultSet rs, final String columnLabel) throws SQLException {
        boolean v = rs.getBoolean(columnLabel);
//		A method that returns either Boolean.TRUE, Boolean.FALSE or null is an accident waiting to happen.
//		This method can be invoked as though it returned a value of type boolean, andthe compiler will insert
//		automatic unboxing of the Boolean value. If a null value is returned,this will result in a NullPointerException.
//
//		if (rs.wasNull()) {
//			return null;
//		}
        return v;
    }

    public static String getString(final ResultSet rs, final String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

    public static byte[] getBytes(final ResultSet rs, final String columnLabel) throws SQLException {
        return rs.getBytes(columnLabel);
    }

    public static Timestamp getTimestamp(final ResultSet rs, final String columnLabel) throws SQLException {
        return rs.getTimestamp(columnLabel);
    }

    public static BigDecimal getBigDecimal(final ResultSet rs) throws SQLException {
        return rs.getBigDecimal(1);
    }

    public static BigDecimal getBigDecimal(final ResultSet rs, final String columnLabel) throws SQLException {
        return rs.getBigDecimal(columnLabel);
    }
}
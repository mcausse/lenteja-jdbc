package org.homs.lechuga.def.handlers.impl;

import org.homs.lechuga.def.handlers.ColumnHandler;
import org.homs.lentejajdbc.ResultSetUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Handlers {

    public static final ColumnHandler STRING = ResultSetUtils::getString;
    public static final ColumnHandler DATE = ResultSetUtils::getTimestamp;
    public static final ColumnHandler BYTE_ARRAY = ResultSetUtils::getBytes;
    public static final ColumnHandler BIG_DECIMAL = ResultSetUtils::getBigDecimal;

    public static final ColumnHandler BOOLEAN = ResultSetUtils::getBoolean;
    public static final ColumnHandler BYTE = ResultSetUtils::getByte;
    public static final ColumnHandler SHORT = ResultSetUtils::getShort;
    public static final ColumnHandler INTEGER = ResultSetUtils::getInteger;
    public static final ColumnHandler LONG = ResultSetUtils::getLong;
    public static final ColumnHandler FLOAT = ResultSetUtils::getFloat;
    public static final ColumnHandler DOUBLE = ResultSetUtils::getDouble;

    public static final ColumnHandler PBOOLEAN = ResultSet::getBoolean;
    public static final ColumnHandler PBYTE = ResultSet::getByte;
    public static final ColumnHandler PSHORT = ResultSet::getShort;
    public static final ColumnHandler PINTEGER = ResultSet::getInt;
    public static final ColumnHandler PLONG = ResultSet::getLong;
    public static final ColumnHandler PFLOAT = ResultSet::getFloat;
    public static final ColumnHandler PDOUBLE = ResultSet::getDouble;

    static final Map<Class<?>, ColumnHandler> HANDLERS = new LinkedHashMap<>();

    static {
        HANDLERS.put(String.class, STRING);
        HANDLERS.put(Date.class, DATE);
        HANDLERS.put(byte[].class, BYTE_ARRAY);
        HANDLERS.put(BigDecimal.class, BIG_DECIMAL);

        HANDLERS.put(Boolean.class, BOOLEAN);
        HANDLERS.put(Byte.class, BYTE);
        HANDLERS.put(Short.class, SHORT);
        HANDLERS.put(Integer.class, INTEGER);
        HANDLERS.put(Long.class, LONG);
        HANDLERS.put(Float.class, FLOAT);
        HANDLERS.put(Double.class, DOUBLE);

        HANDLERS.put(boolean.class, PBOOLEAN);
        HANDLERS.put(byte.class, PBYTE);
        HANDLERS.put(short.class, PSHORT);
        HANDLERS.put(int.class, PINTEGER);
        HANDLERS.put(long.class, PLONG);
        HANDLERS.put(float.class, PFLOAT);
        HANDLERS.put(double.class, PDOUBLE);
    }

    /**
     * Els tipus aquí contemplats especifiquen els que poden tenir les propietats de
     * les entitats a tractar. Per a algun tipus diferent, anotar amb
     * {@see CustomHandler}.
     */
    public static ColumnHandler getHandlerFor(Class<?> type) {
        if (!HANDLERS.containsKey(type)) {
            throw new RuntimeException("unsupported column type: " + type.getName() + ": please specify a concrete "
                    + ColumnHandler.class.getName());
        }
        return HANDLERS.get(type);
    }

}
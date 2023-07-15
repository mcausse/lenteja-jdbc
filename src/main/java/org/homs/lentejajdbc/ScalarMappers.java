package org.homs.lentejajdbc;

import org.homs.lechuga.exception.LechugaException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class ScalarMappers {

    public static final Mapable<String> STRING = ResultSetUtils::getString;
    public static final Mapable<Boolean> BOOLEAN = ResultSetUtils::getBoolean;
    public static final Mapable<Date> TIMESTAMP = ResultSetUtils::getTimestamp;
    public static final Mapable<byte[]> BYTE_ARRAY = ResultSetUtils::getBytes;

    public static final Mapable<Byte> BYTE = ResultSetUtils::getByte;
    public static final Mapable<Short> SHORT = ResultSetUtils::getShort;
    public static final Mapable<Integer> INTEGER = ResultSetUtils::getInteger;
    public static final Mapable<Long> LONG = ResultSetUtils::getLong;
    public static final Mapable<Float> FLOAT = ResultSetUtils::getFloat;
    public static final Mapable<Double> DOUBLE = ResultSetUtils::getDouble;
    public static final Mapable<BigDecimal> BIG_DECIMAL = ResultSetUtils::getBigDecimal;

    public static final Mapable<Byte> PBYTE = (rs) -> rs.getByte(1);
    public static final Mapable<Short> PSHORT = (rs) -> rs.getShort(1);
    public static final Mapable<Integer> PINTEGER = (rs) -> rs.getInt(1);
    public static final Mapable<Long> PLONG = (rs) -> rs.getLong(1);
    public static final Mapable<Float> PFLOAT = (rs) -> rs.getFloat(1);
    public static final Mapable<Double> PDOUBLE = (rs) -> rs.getDouble(1);

    static final Map<Class<?>, Mapable<?>> scalarMappers = new LinkedHashMap<>();

    static {
        scalarMappers.put(String.class, STRING);
        scalarMappers.put(Date.class, TIMESTAMP);
        scalarMappers.put(byte[].class, BYTE_ARRAY);
        scalarMappers.put(BigDecimal.class, BIG_DECIMAL);

        scalarMappers.put(Boolean.class, BOOLEAN);
        scalarMappers.put(Byte.class, BYTE);
        scalarMappers.put(Short.class, SHORT);
        scalarMappers.put(Integer.class, INTEGER);
        scalarMappers.put(Long.class, LONG);
        scalarMappers.put(Float.class, FLOAT);
        scalarMappers.put(Double.class, DOUBLE);

        // scalarMappers.put(boolean.class, PBOOLEAN);
        scalarMappers.put(byte.class, PBYTE);
        scalarMappers.put(short.class, PSHORT);
        scalarMappers.put(int.class, PINTEGER);
        scalarMappers.put(long.class, PLONG);
        scalarMappers.put(float.class, PFLOAT);
        scalarMappers.put(double.class, PDOUBLE);
    }

    @SuppressWarnings("unchecked")
    public static <T> Mapable<T> getScalarMapperFor(Class<?> columnClass) {
        if (!scalarMappers.containsKey(columnClass)) {
            throw new LechugaException("no scalar mapper defined for: " + columnClass.getName());
        }
        return (Mapable<T>) scalarMappers.get(columnClass);
    }

}
package org.homs.lechuga.def.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ColumnHandler {

    /**
     * donat el valor de bean, retorna el valor a tipus de jdbc, apte per a setejar
     * en un {@see org.lenteja.jdbc.query.QueryObject} o en {@see PreparedStatement}.
     */
    default Object getJdbcValue(Object value) {
        return value;
    }

    /**
     * retorna el valor de bean a partir del {@link ResultSet}.
     */
    Object readValue(ResultSet rs, String columnName) throws SQLException;
}
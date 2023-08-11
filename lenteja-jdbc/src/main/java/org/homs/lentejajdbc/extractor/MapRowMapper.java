package org.homs.lentejajdbc.extractor;

import org.homs.lentejajdbc.Mapable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapRowMapper implements Mapable<Map<String, Object>> {

    @Override
    public Map<String, Object> map(ResultSet rs) throws SQLException {
        Map<String, Object> row = new LinkedHashMap<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            String columnName = rs.getMetaData().getColumnName(i);
            Object columnValue = rs.getObject(columnName);
            row.put(columnName, columnValue);
        }
        return row;
    }

}
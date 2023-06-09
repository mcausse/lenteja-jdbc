package org.homs.lentejajdbc.extractor;

import org.homs.lentejajdbc.Mapable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapResultSetExtractor implements Mapable<TableResult> {

    @Override
    public TableResult map(ResultSet rs) throws SQLException {

        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            columnNames.add(rs.getMetaData().getColumnName(i));
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (String col : columnNames) {
                row.put(col, rs.getObject(col));
            }
            rows.add(row);
        }

        return new TableResult(columnNames, rows);
    }

}

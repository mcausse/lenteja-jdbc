package org.homs.lentejajdbc.extractor;

import org.homs.lentejajdbc.Mapable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetPagedExtractor<T> implements Mapable<PageResult<T>> {

    final Mapable<T> rowMapper;
    final Pager pager;

    public ResultSetPagedExtractor(Mapable<T> rowMapper, Pager pager) {
        super();
        this.rowMapper = rowMapper;
        this.pager = pager;
    }

    @Override
    public PageResult<T> map(ResultSet rs) throws SQLException {
        final List<T> r = new ArrayList<>();
        rs.absolute(pager.getPageSize() * pager.getNumPage());
        int k = 0;
        while (rs.next() && k < pager.getPageSize()) {
            r.add(rowMapper.map(rs));
            k++;
        }

        rs.last();
        final int totalRows = rs.getRow();

        int totalPages;
        if (totalRows % pager.getPageSize() == 0) {
            totalPages = totalRows / pager.getPageSize();
        } else {
            totalPages = totalRows / pager.getPageSize() + 1;
        }

        return new PageResult<>(pager, totalRows, totalPages, r);
    }

}
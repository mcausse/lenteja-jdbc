package org.homs.lentejajdbc.extractor;

import java.util.List;

public class PageResult<T> {

    final Pager pager;

    final int totalRows;
    final int totalPages;

    final List<T> page;

    public PageResult(Pager pager, int totalRows, int totalPages, List<T> page) {
        super();
        this.pager = pager;
        this.totalRows = totalRows;
        this.totalPages = totalPages;
        this.page = page;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<T> getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "PageResult [pager=" + pager + ", totalRows=" + getTotalRows() + ", totalPages=" + getTotalPages()
                + ", page=" + getPage() + "]";
    }

}
package org.homs.lentejajdbc.extractor;

public class Pager {

    final int pageSize;
    final int numPage;

    public Pager(int pageSize, int numPage) {
        super();
        this.pageSize = pageSize;
        this.numPage = numPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getNumPage() {
        return numPage;
    }

    @Override
    public String toString() {
        return "Pager [pageSize=" + pageSize + ", numPage=" + numPage + "]";
    }

}

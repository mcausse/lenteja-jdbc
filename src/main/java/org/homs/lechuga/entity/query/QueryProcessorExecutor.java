package org.homs.lechuga.entity.query;

import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.Mapable;
import org.homs.lentejajdbc.exception.EmptyResultException;
import org.homs.lentejajdbc.exception.TooManyResultsException;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.List;

public class QueryProcessorExecutor<E> {

    final DataAccesFacade facade;
    final QueryObject queryObject;
    final Mapable<E> rowMapper;

    public QueryProcessorExecutor(DataAccesFacade facade, QueryObject queryObject, Mapable<E> rowMapper) {
        this.facade = facade;
        this.queryObject = queryObject;
        this.rowMapper = rowMapper;
    }

    public E loadUnique() throws TooManyResultsException, EmptyResultException {
        return facade.loadUnique(queryObject, rowMapper);
    }

    public E loadFirst() throws EmptyResultException {
        return facade.loadFirst(queryObject, rowMapper);
    }

    public List<E> load() {
        return facade.load(queryObject, rowMapper);
    }

    public int update() {
        return facade.update(queryObject);
    }
}

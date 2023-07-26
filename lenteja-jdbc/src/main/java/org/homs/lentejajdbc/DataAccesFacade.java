package org.homs.lentejajdbc;

import org.homs.lentejajdbc.exception.EmptyResultException;
import org.homs.lentejajdbc.exception.TooManyResultsException;
import org.homs.lentejajdbc.query.IQueryObject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DataAccesFacade {

    DataSource getDataSource();

    void close();

    <T> T loadUnique(IQueryObject q, Mapable<T> mapable) throws TooManyResultsException, EmptyResultException;

    <T> T loadFirst(IQueryObject q, Mapable<T> mapable) throws EmptyResultException;

    interface ConnectionExecutor<T> {
        T execute(Connection c) throws SQLException;
    }

    <T> T execute(ConnectionExecutor<T> f);

    <T> List<T> load(IQueryObject q, Mapable<T> mapable);

    int update(IQueryObject q);

    <T> T extract(IQueryObject q, Mapable<T> extractor);

    void begin();

    void commit();

    void rollback();

    boolean isValidTransaction();

    Connection getCurrentConnection();
}

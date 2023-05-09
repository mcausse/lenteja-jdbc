package org.homs.lentejajdbc;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.homs.lentejajdbc.exception.EmptyResultException;
import org.homs.lentejajdbc.exception.TooManyResultsException;
import org.homs.lentejajdbc.query.IQueryObject;

public interface DataAccesFacade {

	DataSource getDataSource();

	void close();

	<T> T loadUnique(IQueryObject q, Mapable<T> mapable) throws TooManyResultsException, EmptyResultException;

	<T> T loadFirst(IQueryObject q, Mapable<T> mapable) throws EmptyResultException;

	<T> List<T> load(IQueryObject q, Mapable<T> mapable);

	int update(IQueryObject q);

	<T> T extract(IQueryObject q, Mapable<T> extractor);

	void begin();

	void commit();

	void rollback();

	boolean isValidTransaction();

	Connection getCurrentConnection();
}

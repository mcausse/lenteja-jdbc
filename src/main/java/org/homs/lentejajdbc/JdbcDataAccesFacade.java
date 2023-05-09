package org.homs.lentejajdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.homs.lentejajdbc.exception.EmptyResultException;
import org.homs.lentejajdbc.exception.JdbcException;
import org.homs.lentejajdbc.exception.TooManyResultsException;
import org.homs.lentejajdbc.query.IQueryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcDataAccesFacade implements DataAccesFacade {

	static final Logger LOG = LoggerFactory.getLogger(JdbcDataAccesFacade.class);

	protected final DataSource ds;

	@Override
	public DataSource getDataSource() {
		return ds;
	}

	protected static class Tx {

		final Connection c;
		final Throwable openedAt;

		public Tx(Connection c) {
			super();
			this.c = c;
			this.openedAt = new Exception();
			configureConnection();
		}

		void configureConnection() {
			try {
				c.setAutoCommit(false);
				c.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			} catch (final SQLException e) {
				throw new JdbcException(e);
			}
		}

		public boolean isValid() throws SQLException {
			return !c.isClosed();
		}

		public Throwable getOpenedAt() {
			return openedAt;
		}
	}

	protected final ThreadLocal<Tx> threadton = ThreadLocal.withInitial(() -> null);

	public JdbcDataAccesFacade(DataSource ds) {
		super();
		this.ds = ds;
	}

	@Override
	public void begin() {
		createConnection();
		LOG.debug("=> begin");
	}

	@Override
	public Connection getCurrentConnection() {
		return getConnection();
	}

	protected void createConnection() {
		if (isValidTransaction()) {
			throw new JdbcException("transaction is yet active", threadton.get().getOpenedAt());
		}
		try {
			threadton.set(new Tx(ds.getConnection()));
		} catch (final SQLException e) {
			throw new JdbcException(e);
		}
	}

	protected Connection getConnection() {
		try {
			Tx tx = threadton.get();
			if (tx == null || !tx.isValid()) {
				throw new JdbcException("not in a valid transaction");
			}
			return tx.c;
		} catch (final SQLException e) {
			throw new JdbcException(e);
		}
	}

	@Override
	public boolean isValidTransaction() {
		try {
			return threadton.get() != null && threadton.get().isValid();
		} catch (SQLException e) {
			throw new JdbcException(e);
		}
	}

	@Override
	public void commit() {
		final Connection c = getConnection();
		try {
			c.commit();
			close();
			LOG.debug("<= commit");
		} catch (final Exception e) {
			throw new JdbcException(e);
		}
	}

	@Override
	public void rollback() {
		final Connection c = getConnection();
		try {
			c.rollback();
			close();
			LOG.debug("<= rollback");
		} catch (final Exception e) {
			throw new JdbcException(e);
		}
	}

	@Override
	public void close() {
		final Connection c = getConnection();
		try {
			c.close();
		} catch (final Exception e) {
			throw new JdbcException(e);
		} finally {
			threadton.remove();
		}
	}

	protected PreparedStatement prepareStatement(Connection c, IQueryObject q) throws SQLException {
		final PreparedStatement ps = c.prepareStatement(q.getQuery());
		try {
			Object[] args = q.getArgs();
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			return ps;
		} catch (final SQLException e) {
			closeResources(null, ps);
			throw e;
		}
	}

	protected void closeResources(ResultSet rs, PreparedStatement ps) {
		if (rs != null) {
			try {
				rs.close();
			} catch (final SQLException e) {
				throw new JdbcException(e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (final SQLException e) {
				throw new JdbcException(e);
			}
		}
	}

	@Override
	public <T> T loadUnique(IQueryObject q, Mapable<T> mapable) throws TooManyResultsException, EmptyResultException {

		LOG.debug("{}", q);
		Connection c;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = prepareStatement(c, q);
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new EmptyResultException(q.toString());
			}
			final T r = mapable.map(rs);
			if (rs.next()) {
				throw new TooManyResultsException(q.toString());
			}
			return r;
		} catch (final EmptyResultException e) {
			throw e;
		} catch (final SQLException e) {
			throw new JdbcException(q.toString(), e);
		} finally {
			closeResources(rs, ps);
		}
	}

	@Override
	public <T> T loadFirst(IQueryObject q, Mapable<T> mapable) throws EmptyResultException {
		LOG.debug("{}", q);
		Connection c;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = prepareStatement(c, q);
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new EmptyResultException(q.toString());
			}
			final T r = mapable.map(rs);
			return r;
		} catch (final EmptyResultException e) {
			throw e;
		} catch (final SQLException e) {
			throw new JdbcException(q.toString(), e);
		} finally {
			closeResources(rs, ps);
		}
	}

	@Override
	public <T> List<T> load(IQueryObject q, Mapable<T> mapable) {
		LOG.debug("{}", q);
		Connection c;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = prepareStatement(c, q);
			rs = ps.executeQuery();
			final List<T> r = new ArrayList<T>();
			while (rs.next()) {
				r.add(mapable.map(rs));
			}
			return r;
		} catch (final SQLException e) {
			throw new JdbcException(q.toString(), e);
		} finally {
			closeResources(rs, ps);
		}
	}

	@Override
	public int update(final IQueryObject q) {
		Connection c;
		PreparedStatement ps = null;
		int affectedRows = -1;
		try {
			c = getConnection();
			ps = prepareStatement(c, q);
			affectedRows = ps.executeUpdate();
			return affectedRows;
		} catch (final SQLException e) {
			throw new JdbcException(q.toString(), e);
		} finally {
			LOG.debug("{} => {}", q, affectedRows);
			closeResources(null, ps);
		}
	}

	@Override
	public <T> T extract(final IQueryObject q, Mapable<T> extractor) {
		LOG.debug("{}", q);
		Connection c;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(q.getQuery(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Object[] args = q.getArgs();
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();

			return extractor.map(rs);

		} catch (final SQLException e) {
			throw new JdbcException(q.toString(), e);
		} finally {
			closeResources(rs, ps);
		}
	}
}
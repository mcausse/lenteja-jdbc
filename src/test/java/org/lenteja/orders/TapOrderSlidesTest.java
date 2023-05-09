package org.lenteja.orders;

import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.Mapable;
import org.homs.lentejajdbc.ResultSetUtils;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Before;
import org.junit.Test;
import org.lenteja.orders.ent.*;

import java.util.List;
import java.util.Map;

import static org.homs.lentejajdbc.ResultSetUtils.extractRowAsMap;
import static org.homs.lentejajdbc.query.QueryObjectUtils.queryFor;

public class TapOrderSlidesTest {

    final DataAccesFacade facade;

    public TapOrderSlidesTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:tapOrderSlides");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
    }

    @Before
    public void before() {
        facade.begin();
        try {
            new SqlScriptExecutor(facade).runFromClasspath("tapOrderSlides.sql");
            facade.commit();
        } catch (Throwable e) {
            facade.rollback();
            throw e;
        }
    }

    @Test
    public void name() {
        facade.begin();

        var repository = new TapOrderSlidesRepository(facade);
        var patients = repository.getPatients();

        System.out.println(patients);

        facade.rollback();
    }
}

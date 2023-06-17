package org.lentejajdbc.orders;

import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TapOrderSlidesTest {

    final DataAccesFacade facade;

    public TapOrderSlidesTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:tapOrderSlides");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
    }

    @BeforeEach
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

        for (var p : patients) {
            System.out.println(p.PatientID1);
            for (var o : p.orders) {
                System.out.println("  + " + o.sampleId);
                for (var object : o.objects) {
                    System.out.println("    o " + object.id);
                }
                for (var c : o.containers) {
                    System.out.println("    + " + c.ContainerID);
                    for (var object : c.objects) {
                        System.out.println("      o " + object.id);
                    }
                    for (var b : c.blocks) {
                        System.out.println("      + " + b.BlockID);
                        for (var object : b.objects) {
                            System.out.println("        o " + object.id);
                        }
                        for (var s : b.slides) {
                            System.out.println("        + " + s.SlideID);
                            for (var object : s.objects) {
                                System.out.println("          o " + object.id);
                            }
                        }
                    }
                }
            }
        }

        facade.rollback();
    }
}

package org.homs.lechuga.entity;

import org.homs.lechuga.Colr405;
import org.homs.lechuga.Colr405Id;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Colr405Test {

    final DataAccesFacade facade;

    public Colr405Test() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:a");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
    }

    @BeforeEach
    public void before() {
        facade.begin();
        try {
            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
            sql.runFromClasspath("colr405.sql");
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }
    }

    @Test
    void load_all_dogs() {
        EntityManager<Colr405,Colr405Id> manager = new EntityManagerBuilder().buildEntityManager(facade, Colr405.class);

        String uuid = "315f8695";
        var c1 = new Colr405(new Colr405Id(uuid, 1), "jou1");
        var c2 = new Colr405(new Colr405Id(uuid, 2), "jou2");
        var c3 = new Colr405(new Colr405Id(uuid, 3), "jou3");
        var c4 = new Colr405(new Colr405Id("dee84c18e3a6", 1), "juas");

        facade.begin();
        try {
            manager.store(c1);
            manager.store(c2);
            manager.store(c3);
            manager.store(c4);
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }

        List<Colr405> colrs;
        facade.begin();
        try {
            // Act
            colrs = manager.loadAll();
        } finally {
            facade.rollback();
        }

        assertThat(colrs)
                .hasSize(4)
                .hasToString(
                        "[Colr405{id=Colr405Id{guid='315f8695', version=1}, value='jou1'}, " +
                                "Colr405{id=Colr405Id{guid='315f8695', version=2}, value='jou2'}, " +
                                "Colr405{id=Colr405Id{guid='315f8695', version=3}, value='jou3'}, " +
                                "Colr405{id=Colr405Id{guid='dee84c18e3a6', version=1}, value='juas'}]"
                );

        facade.begin();
        try {
            // Act
            var colr = manager.loadById(new Colr405Id(uuid, 2) );
            assertThat(colr).hasToString("Colr405{id=Colr405Id{guid='315f8695', version=2}, value='jou2'}");
        } finally {
            facade.rollback();
        }
    }

}
package org.homs.lechuga.entity.query;

import org.homs.lechuga.*;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class QueryProcessorTest {

    final DataAccesFacade facade;

    public QueryProcessorTest() {
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
            sql.runFromClasspath("dogs_and_persons.sql");
            sql.runFromClasspath("colr405.sql");
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }
    }

    @Test
    void name1() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder(null).build(Dog.class);
        var qp = new QueryProcessor<Dog>(null, null);

        qp
                .addAlias("d", dogManager)
                .append("select {d.*} from {d} where {d.name=?}", "faria");


        assertThat(qp.getQueryObject()).hasToString(
                "select id_dog, age, chip_num, name from dog d where d.name=? -- [faria(String)]");
    }

    @Test
    void name2() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder(null).build(Dog.class);
        var qp = new QueryProcessor<Dog>(null, null);

        qp
                .addAlias("d", dogManager)
                .append("select {d.*} from {d} where {d.age between ? and ?}", 10, 20);


        assertThat(qp.getQueryObject()).hasToString(
                "select id_dog, age, chip_num, name from dog d where d.age between ? and ? -- [10(Integer), 20(Integer)]");
    }

    @Test
    void name3() {
        EntityManager<Person, String> personManager = new EntityManagerBuilder(null).build(Person.class);
        var qp = new QueryProcessor<Dog>(null, null);

        qp
                .addAlias("p", personManager)
                .append("select {p.*} from {p} where {p.sex in (?, ?)}", ESex.MALE, ESex.FEMALE);


        assertThat(qp.getQueryObject()).hasToString(
                "select guid, age, first_name, sur_name, genre from persons p where p.genre in (?, ?) -- [MALE(String), FEMALE(String)]");
    }

    @Test
    void name4() {
        EntityManager<Person, String> personManager = new EntityManagerBuilder(null).build(Person.class);
        var qp = new QueryProcessor<Dog>(null, null);

        qp
                .addAlias("p", personManager)
                .append("select {p.*} from {p} where {p.sex} in ({p.:sex?}, {p.:sex?})", ESex.MALE, ESex.FEMALE);


        assertThat(qp.getQueryObject()).hasToString(
                "select guid, age, first_name, sur_name, genre from persons p where p.genre in (?, ?) -- [MALE(String), FEMALE(String)]");
    }

    @Test
    void name5() {
        EntityManager<Colr405, Colr405Id> man = new EntityManagerBuilder(facade).build(Colr405.class);

        facade.begin();
        String uuid = "315f8695";
        var c1 = new Colr405(new Colr405Id(uuid, 1), "jou1");
        var c2 = new Colr405(new Colr405Id(uuid, 2), "jou2");
        var c3 = new Colr405(new Colr405Id(uuid, 3), "jou3");
        var c4 = new Colr405(new Colr405Id("dee84c18e3a6", 1), "juas");
        man.storeAll(c1, c2, c3, c4);
        facade.commit();

        facade.begin();
        var r = man.createQuery("c").append("select {c.*} from {c} where {c.id.version>0}").execute().load();
        facade.rollback();

        assertThat(r).hasSize(4);
    }
}

package org.homs.lechuga.entity.query;

import org.homs.lechuga.Dog;
import org.homs.lechuga.ESex;
import org.homs.lechuga.Person;
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

//    final DataAccesFacade facade;
//
//    public QueryProcessorTest() {
//        final JDBCDataSource ds = new JDBCDataSource();
//        ds.setUrl("jdbc:hsqldb:mem:a");
//        ds.setUser("sa");
//        ds.setPassword("");
//        this.facade = new JdbcDataAccesFacade(ds);
//    }
//
//    @BeforeEach
//    public void before() {
//        facade.begin();
//        try {
//            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
//            sql.runFromClasspath("dogs_and_persons.sql");
//            sql.runFromClasspath("colr405.sql");
//            facade.commit();
//        } catch (Exception e) {
//            facade.rollback();
//            throw e;
//        }
//    }

    @Test
    void name1() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder(null).build(Dog.class);
        var qp = new QueryProcessor();

        qp
                .addAlias("d", dogManager)
                .append("select {d.*} from {d} where {d.name=?}", "faria");


        assertThat(qp.getQueryObject()).hasToString(
                "select id_dog, age, chip_num, name from dog d where d.name=? -- [faria(String)]");
    }

    @Test
    void name2() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder(null).build(Dog.class);
        var qp = new QueryProcessor();

        qp
                .addAlias("d", dogManager)
                .append("select {d.*} from {d} where {d.age between ? and ?}", 10, 20);


        assertThat(qp.getQueryObject()).hasToString(
                "select id_dog, age, chip_num, name from dog d where d.age between ? and ? -- [10(Integer), 20(Integer)]");
    }

    @Test
    void name3() {
        EntityManager<Person, String> personManager = new EntityManagerBuilder(null).build(Person.class);
        var qp = new QueryProcessor();

        qp
                .addAlias("p", personManager)
                .append("select {p.*} from {p} where {p.sex in (?, ?)}", ESex.MALE, ESex.FEMALE);


        assertThat(qp.getQueryObject()).hasToString(
                "select guid, age, first_name, sur_name, genre from persons p where p.genre in (?, ?) -- [MALE(String), FEMALE(String)]");
    }
    @Test
    void name4() {
        EntityManager<Person, String> personManager = new EntityManagerBuilder(null).build(Person.class);
        var qp = new QueryProcessor();

        qp
                .addAlias("p", personManager)
                .append("select {p.*} from {p} where {p.sex} in ({p.:sex?}, {p.:sex?})", ESex.MALE, ESex.FEMALE);


        assertThat(qp.getQueryObject()).hasToString(
                "select guid, age, first_name, sur_name, genre from persons p where p.genre in (?, ?) -- [MALE(String), FEMALE(String)]");
    }
}

package org.lentejajdbc;

import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.query.QueryObject;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LentejaTest {

    final DataAccesFacade facade;

    public LentejaTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:pizza");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
    }

    @BeforeEach
    public void before() {
        facade.begin();
        try {
            facade.update(new QueryObject("drop table pizzas if exists;"));
            facade.update(new QueryObject(
                    "create table pizzas (id_pizza integer primary key generated by default as identity(start with 10), "
                            + "name varchar(15) not null, price numeric(15,2) not null);"));
            facade.commit();
        } catch (Throwable e) {
            facade.rollback();
            throw e;
        }
    }

    public static class Pizza {

        public Long idPizza;
        public String name;
        public Double price;

        @Override
        public String toString() {
            return "Pizza [idPizza=" + idPizza + ", name=" + name + ", price=" + price + "]";
        }
    }

    @Test
    public void testName() {

        facade.begin();
        try {

            Pizza napolitana = new Pizza();
            napolitana.name = "napolitana";
            napolitana.price = 10.25;

            assertEquals("Pizza [idPizza=null, name=napolitana, price=10.25]", napolitana.toString());

            QueryObject q = new QueryObject();
            q.append("INSERT INTO pizzas (name, price) VALUES (?,?)");
            q.addArg(napolitana.name);
            q.addArg(napolitana.price);
            facade.update(q);

            napolitana.idPizza = facade.loadUnique(new QueryObject("call identity()"), (rs) -> rs.getLong(1));
            assertEquals("Pizza [idPizza=10, name=napolitana, price=10.25]", napolitana.toString());

            List<Pizza> allPizzas = facade.load(new QueryObject("SELECT * FROM pizzas"), (rs) -> {
                Pizza p = new Pizza();
                p.idPizza = rs.getLong("id_pizza");
                p.name = rs.getString("name");
                p.price = rs.getDouble("price");
                return p;
            });

            assertEquals("[Pizza [idPizza=10, name=napolitana, price=10.25]]", allPizzas.toString());

            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }
    }

}

package org.homs.lechuga.kv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.TransactionalOps;
import org.homs.lentejajdbc.query.QueryObject;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KvRepositoryTest {

    private JdbcDataAccesFacade facade;
    private TransactionalOps transactionalOps;

    private KvRepository<Dog> dogRepository;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Dog {

        long id;
        String name;
        int age;
    }

    @BeforeEach
    void setUp() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:file:jou;shutdown=true");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
        this.transactionalOps = new TransactionalOps(facade);

        transactionalOps.run(() ->
                facade.update(QueryObject.of("drop table " + KvRepository.getEntityTableName(Dog.class) + " if exists;")));

        this.dogRepository = new KvRepository<>(facade, Dog.class, dog -> String.valueOf(dog.getId()));
    }

    @Test
    void name() {

        transactionalOps.run(() -> {
            final Dog chucho = new Dog(1L, "chucho", 13);

            // Act
            dogRepository.store(chucho);
            dogRepository.store(chucho);
            final Dog chucho2 = dogRepository.loadBy(String.valueOf(chucho.getId()));

            assertThat(chucho2).isEqualTo(chucho);
        });
    }
}
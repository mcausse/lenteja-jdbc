package org.homs.lentejajdbc.script;

import org.homs.lentejajdbc.IJdbcFacade;
import org.homs.lentejajdbc.JdbcIJdbcFacade;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class SqlStatementParserTest {

    final IJdbcFacade facade;

    public SqlStatementParserTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:pizza");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcIJdbcFacade(ds);
    }

    static Stream<Arguments> scriptProvider() {
        return Stream.of(
                Arguments.of("dogs_and_masters.sql"),
                Arguments.of("movielens.sql"),
                Arguments.of("pizza_exps.sql"),
                Arguments.of("tapOrderSlides.sql"),
                Arguments.of("votr.sql")
        );
    }

    @ParameterizedTest
    @MethodSource("scriptProvider")
    public void test_script(String script) {
        facade.begin();
        try {
            new SqlScriptExecutor(facade).runFromClasspath(script);
            facade.commit();
        } catch (Throwable e) {
            facade.rollback();
            throw e;
        }
    }

}
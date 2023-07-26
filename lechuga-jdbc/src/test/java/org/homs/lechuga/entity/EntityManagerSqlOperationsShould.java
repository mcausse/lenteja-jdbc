package org.homs.lechuga.entity;

import org.homs.lechuga.Colr405;
import org.homs.lechuga.Colr405Id;
import org.homs.lechuga.Dog;
import org.homs.lechuga.Person;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityManagerSqlOperationsShould {

    @Test
    void generate_load_by_id_query_for_person() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Person.class));

        var qo = em.queryForLoadById("abcdefgh");

        assertThat(qo).hasToString("select guid,age,first_name,sur_name,genre from persons where guid=? -- [abcdefgh(String)]");
    }

    @Test
    void generate_load_by_id_query_for_dog() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Dog.class));

        var qo = em.queryForLoadById(123L);

        assertThat(qo).hasToString("select id_dog,age,chip_num,name from dog where id_dog=? -- [123(Long)]");
    }


    @Test
    void generate_load_by_id_query_for_colr405() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Colr405.class));

        var qo = em.queryForLoadById(new Colr405Id("abcdefgh", 1));

        assertThat(qo).hasToString("select guid,version,value from colr405 where guid=? and version=? -- [abcdefgh(String), 1(Integer)]");
    }

    @Test
    void generate_load_all_query_for_colr405() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Colr405.class));

        var qo = em.queryForLoadAll(Order.by(Order.asc("id.version"), Order.desc("value")).toArray(new Order[]{}));

        assertThat(qo).hasToString("select guid,version,value from colr405 order by version asc, value desc -- []");
    }

    @Test
    void generate_load_by_prop_query_for_colr405() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Colr405.class));

        var qo = em.queryForLoadByProp(
                "id.version",
                123,
                new Order[]{});

        assertThat(qo).hasToString("select guid,version,value from colr405 where version=? -- [123(Integer)]");
    }

    @Test
    void generate_update_query_for_colr405() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Colr405.class));

        var qo = em.queryForUpdate(new Colr405(new Colr405Id("abcdefgh", 1), "jou1"));

        assertThat(qo).hasToString("update colr405 set value=? where guid=? and version=? -- [jou1(String), abcdefgh(String), 1(Integer)]");
    }

    @Test
    void generate_update_query_for_dog() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Dog.class));

        var qo = em.queryForUpdate(new Dog(123L, "abcdefgh", "chuchales", 13));

        assertThat(qo).hasToString("update dog set age=?,chip_num=?,name=? where id_dog=? -- [13(Integer), abcdefgh(String), chuchales(String), 123(Long)]");
    }

    @Test
    void generate_exists_query_for_colr405() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Colr405.class));

        var qo = em.queryForExists(new Colr405(new Colr405Id("abcdefgh", 1), "jou1"));

        assertThat(qo).hasToString("select count(*) from colr405 where guid=? and version=? -- [abcdefgh(String), 1(Integer)]");
    }

    @Test
    void generate_exists_By_id_query_for_colr405() {
        var em = new EntityManagerSqlOperations(new EntityModelBuilder().build(Colr405.class));

        var qo = em.queryForExistsById(new Colr405Id("abcdefgh", 1));

        assertThat(qo).hasToString("select count(*) from colr405 where guid=? and version=? -- [abcdefgh(String), 1(Integer)]");
    }
}

package org.homs.lechuga.entity;

import org.homs.lechuga.Dog;
import org.homs.lechuga.ESex;
import org.homs.lechuga.Person;
import org.homs.lechuga.PersonName;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EntityManagerBuilderShould {

    final DataAccesFacade facade;

    public EntityManagerBuilderShould() {
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
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }
    }

    @Test
    void load_all_dogs() {
        var dogManager = new EntityManagerBuilder().buildEntityManager(facade, Dog.class);

        List<Dog> dogs;
        facade.begin();
        try {
            // Act
            dogs = dogManager.loadAll();
        } finally {
            facade.rollback();
        }

        assertThat(dogs)
                .hasSize(3)
                .hasToString(
                        "[Dog{id=10, chipId='aaa', name='faria', age=12}, Dog{id=11, chipId='bbb', name='din', age=13}, Dog{id=12, chipId='bbb', name='chucho', age=14}]"
                );
    }

    @Test
    void load_all_persons_with_order() {
        var personManager = new EntityManagerBuilder().buildEntityManager(facade, Person.class);

        facade.begin();
        try {
            personManager.store(new Person(new PersonName("m", "h"), 41, ESex.MALE));
            personManager.store(new Person(new PersonName("m", "e"), 45, ESex.FEMALE));
            personManager.store(new Person(new PersonName("a", "v"), 44, ESex.FEMALE));
            personManager.store(new Person(new PersonName("a", "r"), 38, ESex.FEMALE));
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }

        List<Person> personAsc;
        List<Person> personDesc;
        facade.begin();
        try {
            // Act
            personAsc = personManager.loadAll(Order.asc("age"), Order.asc("name.firstName"));
            personDesc = personManager.loadAll(Order.desc("age"), Order.desc("name.firstName"));
        } finally {
            facade.rollback();
        }

        assertThat(personAsc).hasToString(
                "[Person{guid='103', name=PersonName{firstName='a', surName='r'}, age=38, sex=FEMALE}, Person{guid='100', name=PersonName{firstName='m', surName='h'}, age=41, sex=MALE}, Person{guid='102', name=PersonName{firstName='a', surName='v'}, age=44, sex=FEMALE}, Person{guid='101', name=PersonName{firstName='m', surName='e'}, age=45, sex=FEMALE}]"
        );
        assertThat(personDesc).hasToString(
                "[Person{guid='101', name=PersonName{firstName='m', surName='e'}, age=45, sex=FEMALE}, Person{guid='102', name=PersonName{firstName='a', surName='v'}, age=44, sex=FEMALE}, Person{guid='100', name=PersonName{firstName='m', surName='h'}, age=41, sex=MALE}, Person{guid='103', name=PersonName{firstName='a', surName='r'}, age=38, sex=FEMALE}]"
        );
    }

    @Test
    void delete_dog_by_id() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder().buildEntityManager(facade, Dog.class);

        facade.begin();
        try {
            // Act
            dogManager.deleteById(10L);
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }

        List<Dog> dogs;
        facade.begin();
        try {
            // Act
            dogs = dogManager.loadAll();
        } finally {
            facade.rollback();
        }
        assertThat(dogs)
                .hasSize(2)
                .hasToString(
                        "[Dog{id=11, chipId='bbb', name='din', age=13}, Dog{id=12, chipId='bbb', name='chucho', age=14}]"
                );
    }


    @Test
    void load_dog_by_id() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder().buildEntityManager(facade, Dog.class);

        Dog dog;
        facade.begin();
        try {
            // Act
            dog = dogManager.loadById(10L);
        } finally {
            facade.rollback();
        }
        assertThat(dog).hasToString(
                "Dog{id=10, chipId='aaa', name='faria', age=12}"
        );
    }

    @Test
    void store_dog() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder().buildEntityManager(facade, Dog.class);

        Dog dog = new Dog("xyz", "blanca", 8);

        facade.begin();
        try {

            // Act
            dogManager.store(dog);

            dog.setAge(9);

            // Act
            dogManager.store(dog);

            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }

        facade.begin();
        try {
            dog = dogManager.loadById(dog.getId());
        } finally {
            facade.rollback();
        }
        assertThat(dog).hasToString(
                "Dog{id=13, chipId='xyz', name='blanca', age=9}"
        );
    }

    @Test
    void store_person() {
        EntityManager<Person, String> personManager = new EntityManagerBuilder().buildEntityManager(facade, Person.class);

        Person person = new Person(new PersonName("m", "h"), 41, ESex.MALE);

        facade.begin();
        try {

            // Act
            personManager.store(person);

            person.setAge(35);
            person.getName().setFirstName("martins");

            // Act
            personManager.store(person);

            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }

        facade.begin();
        try {
            person = personManager.loadById(person.getGuid());
        } finally {
            facade.rollback();
        }
        assertThat(person).hasToString(
                "Person{guid='100', name=PersonName{firstName='martins', surName='h'}, age=35, sex=MALE}"
        );
    }

    @Test
    void delete_dog_by_entity() {
        EntityManager<Dog, Long> dogManager = new EntityManagerBuilder().buildEntityManager(facade, Dog.class);

        facade.begin();
        try {
            // Act
            var dog = dogManager.loadById(10L);
            dogManager.delete(dog);
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }

        List<Dog> dogs;
        facade.begin();
        try {
            // Act
            dogs = dogManager.loadAll();
        } finally {
            facade.rollback();
        }
        assertThat(dogs)
                .hasSize(2)
                .hasToString(
                        "[Dog{id=11, chipId='bbb', name='din', age=13}, Dog{id=12, chipId='bbb', name='chucho', age=14}]"
                );
    }

    @Test
    void load_persons_by_property() {
        var personManager = new EntityManagerBuilder().buildEntityManager(facade, Person.class);

        facade.begin();
        try {
            personManager.store(new Person(new PersonName("m", "h"), 41, ESex.MALE));
            personManager.store(new Person(new PersonName("m", "e"), 45, ESex.FEMALE));
            personManager.store(new Person(new PersonName("a", "v"), 44, ESex.FEMALE));
            personManager.store(new Person(new PersonName("a", "r"), 38, ESex.FEMALE));
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }

        List<Person> persons;
        facade.begin();
        try {
            // Act
            persons = personManager.loadByProperty("name.firstName", "m", Order.asc("age"));
        } finally {
            facade.rollback();
        }

        assertThat(persons).hasToString(
                "[Person{guid='100', name=PersonName{firstName='m', surName='h'}, age=41, sex=MALE}, " +
                        "Person{guid='101', name=PersonName{firstName='m', surName='e'}, age=45, sex=FEMALE}]"
        );
    }
}
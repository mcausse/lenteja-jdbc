package org.homs.lechuga.entity;

import org.homs.lechuga.Dog;
import org.homs.lechuga.Person;
import org.homs.lechuga.entity.anno.Embedded;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class EntityManagerModelBuilderShould {

    String toString(List<EntityPropertyModel> beanProperties) {
        return new TreeSet<>(
                beanProperties.stream()
                        .map(EntityPropertyModel::getPropertiesPath)
                        .collect(Collectors.toSet())).toString();
    }

    public static class PersonDog {

        @Embedded
        Person person;
        @Embedded
        Dog dog;

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public Dog getDog() {
            return dog;
        }

        public void setDog(Dog dog) {
            this.dog = dog;
        }

        @Override
        public String toString() {
            return "PersonDog{" +
                    "person=" + person +
                    ", dog=" + dog +
                    '}';
        }
    }

    @Test
    void properly_build_Dog_entity() {

        // Act
        EntityModel<Dog> entityModel = new EntityManagerBuilder().buildEntityModel(Dog.class);

        assertThat(entityModel.getEntityClass()).isEqualTo(Dog.class);
        assertThat(entityModel.getTableName()).isEqualTo("dog");
    }

    @Test
    void properly_build_Person_entity() {

        // Act
        EntityModel<Person> entityModel = new EntityManagerBuilder().buildEntityModel(Person.class);

        assertThat(entityModel.getEntityClass()).isEqualTo(Person.class);
        assertThat(entityModel.getTableName()).isEqualTo("persons");
    }

    @Test
    void work_well_for_Dog() {

        // Act
        var r = new EntityManagerBuilder().buildPropertiesForEntityClass(Dog.class);

        assertThat(toString(r)).isEqualTo("[age, chipId, id, name]");
    }

    @Test
    void work_well_for_Person() {

        // Act
        var r = new EntityManagerBuilder().buildPropertiesForEntityClass(Person.class);

        assertThat(toString(r)).isEqualTo("[age, guid, name.firstName, name.surName, sex]");
    }

    @Test
    void work_well_for_PersonDog() {

        // Act
        var r = new EntityManagerBuilder().buildPropertiesForEntityClass(PersonDog.class);

        assertThat(toString(r)).isEqualTo("[dog.age, dog.chipId, dog.id, dog.name, person.age, person.guid, person.name.firstName, person.name.surName, person.sex]");
    }

    @Test
    void get_and_set_values_in_the_properties_tree() {

        var r = new EntityManagerBuilder().buildPropertiesForEntityClass(PersonDog.class);
        var prop = r.stream()
                .filter(p -> p.getPropertiesPath().equals("person.name.firstName")).
                        reduce((a, b) -> {
                            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                        })
                .get();
        var personDog = new PersonDog();
        assertThat(personDog).hasToString("PersonDog{person=null, dog=null}");

        assertThat(prop.getValue(personDog)).isNull();
        assertThat(personDog).hasToString("PersonDog{person=null, dog=null}");

        prop.setValue(personDog, "mhc");
        assertThat(prop.getValue(personDog)).isEqualTo("mhc");
        assertThat(personDog).hasToString("PersonDog{person=Person{guid='null', name=PersonName{firstName='mhc', surName='null'}, age=0, sex=null}, dog=null}");
    }
}
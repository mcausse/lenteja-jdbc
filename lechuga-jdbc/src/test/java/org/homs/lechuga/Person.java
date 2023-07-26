package org.homs.lechuga;

import org.homs.lechuga.entity.anno.*;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

@Table("persons")
public class Person {

    @Id
    @Generated(value = HsqldbSequence.class, args = {"seq_person"})
    String guid;

    @Embedded
    PersonName name;

    int age;

    @Column("genre")
    @Enumerated
    ESex sex;

    public Person() {
    }

    public Person(PersonName name, int age, ESex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public PersonName getName() {
        return name;
    }

    public void setName(PersonName name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ESex getSex() {
        return sex;
    }

    public void setSex(ESex sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "guid='" + guid + '\'' +
                ", name=" + name +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}

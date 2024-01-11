package org.homs.lechuga;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.homs.lechuga.entity.anno.*;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

@Data
@NoArgsConstructor
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

    public Person(PersonName name, int age, ESex sex) {
        this.name = name;
        this.age = age;
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

package org.homs.lechuga;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbIdentity;

@Table
public class Dog {

    @Id
    @Generated(HsqldbIdentity.class)
    @Column("id_dog")
    Long id;

    @Column("chip_num")
    String chipId;

    String name;

    int age;

    public Dog() {
    }

    public Dog(String chipId, String name, int age) {
        this.chipId = chipId;
        this.name = name;
        this.age = age;
    }

    public Dog(Long id, String chipId, String name, int age) {
        this.id = id;
        this.chipId = chipId;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChipId() {
        return chipId;
    }

    public void setChipId(String chipId) {
        this.chipId = chipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", chipId='" + chipId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

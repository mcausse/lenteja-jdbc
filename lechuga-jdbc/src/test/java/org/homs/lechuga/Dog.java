package org.homs.lechuga;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbIdentity;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public Dog(String chipId, String name, int age) {
        this.chipId = chipId;
        this.name = name;
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

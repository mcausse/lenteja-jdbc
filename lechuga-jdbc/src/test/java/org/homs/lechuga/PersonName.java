package org.homs.lechuga;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.homs.lechuga.entity.anno.Transient;

@Data
@NoArgsConstructor
public class PersonName {

    public String firstName;
    public String surName;

    @Transient
    public String pepito;

    public PersonName(String firstName, String surName) {
        this.firstName = firstName;
        this.surName = surName;
    }

    @Override
    public String toString() {
        return "PersonName{" +
                "firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                '}';
    }
}

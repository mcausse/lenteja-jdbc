package org.homs.lechuga;

import org.homs.lechuga.def.Transient;

public class PersonName {

    public String firstName;
    public String surName;

    @Transient
    public String pepito;

    public PersonName() {
    }

    public PersonName(String firstName, String surName) {
        this.firstName = firstName;
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPepito() {
        return pepito;
    }

    public void setPepito(String pepito) {
        this.pepito = pepito;
    }

    @Override
    public String toString() {
        return "PersonName{" +
                "firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                '}';
    }
}

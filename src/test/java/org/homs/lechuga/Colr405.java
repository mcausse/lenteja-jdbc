package org.homs.lechuga;

import org.homs.lechuga.entity.anno.CompositeId;

public class Colr405 {

    @CompositeId
    Colr405Id id;

    String value;

    public Colr405() {
    }

    public Colr405(Colr405Id id, String value) {
        this.id = id;
        this.value = value;
    }

    public Colr405Id getId() {
        return id;
    }

    public void setId(Colr405Id id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Colr405{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}

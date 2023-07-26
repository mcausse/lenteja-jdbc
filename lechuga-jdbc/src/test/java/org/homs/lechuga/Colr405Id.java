package org.homs.lechuga;

public class Colr405Id {

    String guid;
    Integer version;

    public Colr405Id() {
    }

    public Colr405Id(String guid, Integer version) {
        this.guid = guid;
        this.version = version;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Colr405Id{" +
                "guid='" + guid + '\'' +
                ", version=" + version +
                '}';
    }
}
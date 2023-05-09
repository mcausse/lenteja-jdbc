package org.lenteja.orders.ent;

import java.util.List;
import java.util.Map;

public class Patient {

    public Integer id;
    public String PatientID1;
    public String FirstName;
    public String MiddleName;
    public String LastName;
    public Map<String, Object> allValues;

    public List<Order> orders;

    @Override
    public String toString() {
        return "Patient{" +
                "PatientID1='" + PatientID1 + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", MiddleName='" + MiddleName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", orders=" + orders +
                '}';
    }
}
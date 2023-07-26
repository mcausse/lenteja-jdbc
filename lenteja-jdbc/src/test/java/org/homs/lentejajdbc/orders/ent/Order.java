package org.homs.lentejajdbc.orders.ent;

import java.util.List;
import java.util.Map;

public class Order {

    public Integer id;
    public String sampleId;

    public Map<String, Object> allValues;

    public List<Container> containers;
    public List<Slide> slides;
    public List<OrderObject> objects;

    @Override
    public String toString() {
        return "Order{" +
                "sampleId='" + sampleId + '\'' +
                ", containers=" + containers +
                ", slides=" + slides +
                '}';
    }
}
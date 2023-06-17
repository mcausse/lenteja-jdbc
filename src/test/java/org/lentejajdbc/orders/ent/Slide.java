package org.lentejajdbc.orders.ent;

import java.util.List;
import java.util.Map;

public class Slide {

    public Integer id;
    public String SlideID;

    public Map<String, Object> allValues;
    public List<OrderObject> objects;

    @Override
    public String toString() {
        return SlideID;
    }
}
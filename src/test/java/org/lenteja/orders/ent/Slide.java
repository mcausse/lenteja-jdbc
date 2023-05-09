package org.lenteja.orders.ent;

import java.util.Map;

public class Slide {

    public Integer id;
    public String SlideID;

    public Map<String, Object> allValues;

    @Override
    public String toString() {
        return SlideID;
    }
}
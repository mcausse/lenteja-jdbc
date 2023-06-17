package org.lentejajdbc.orders.ent;

import java.util.List;
import java.util.Map;


public class Block {

    public Integer id;
    public String BlockID;

    public Map<String, Object> allValues;

    public List<Slide> slides;
    public List<OrderObject> objects;

    @Override
    public String toString() {
        return "Block{" +
                "BlockID='" + BlockID + '\'' +
                ", slides=" + slides +
                '}';
    }
}
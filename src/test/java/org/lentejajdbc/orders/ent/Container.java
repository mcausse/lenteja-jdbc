package org.lentejajdbc.orders.ent;

import java.util.List;
import java.util.Map;


public class Container {

    public Integer id;
    public String ContainerID;

    public Map<String, Object> allValues;

    public List<Block> blocks;
    public List<Slide> slides;
    public List<OrderObject> objects;

    @Override
    public String toString() {
        return "Container{" +
                "ContainerID='" + ContainerID + '\'' +
                ", blocks=" + blocks +
                ", slides=" + slides +
                '}';
    }
}

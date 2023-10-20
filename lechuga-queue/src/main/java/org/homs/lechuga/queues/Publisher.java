package org.homs.lechuga.queues;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lentejajdbc.DataAccesFacade;

public class Publisher {

    private final EntityManager<Event, String> entityManager;

    public Publisher(DataAccesFacade facade) {
        this.entityManager = new EntityManagerBuilder(facade).build(Event.class);
    }

    public void publish(Event event) {
        entityManager.insert(event);
    }
}

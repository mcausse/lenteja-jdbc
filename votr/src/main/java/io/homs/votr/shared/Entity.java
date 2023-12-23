package io.homs.votr.shared;

import org.homs.lechuga.queues.Event;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private final List<Event> events = new ArrayList<>();

    public void publish(Event events) {
        this.events.add(events);
    }

    public List<Event> getEvents() {
        return events;
    }
}

package org.homs.lechuga.queues;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Subscriber {

    private final EventsRepository eventsRepository;
    private final Timer timer;
    private final Map<Class<?>, List<Consumer<Event>>> eventConsumers;

    public Subscriber(EventsRepository eventsRepository, int refreshPeriodMs) {
        this.eventsRepository = eventsRepository;
        this.timer = new Timer(refreshPeriodMs, e -> run());
        this.eventConsumers = new LinkedHashMap<>();
    }

    public void subscribe(Class<?> eventType, Consumer<Event> eventConsumer) {
        if (!eventConsumers.containsKey(eventType)) {
            eventConsumers.put(eventType, new ArrayList<>());
        }
        eventConsumers.get(eventType).add(eventConsumer);
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    protected void run() {
        List<Event> events = eventsRepository.loadEventsFromQueue(eventConsumers.keySet());
        for (var event : events) {
            processEvent(event);
        }
    }

    protected void processEvent(Event event) {
        if (eventConsumers.containsKey(event.getType())) {
            for (var consumer : eventConsumers.get(event.getType())) {
                try {
                    consumer.accept(event);
                } catch (Throwable e) {
                    e.printStackTrace(); // TODO log
                }
            }
        }
        eventsRepository.delete(event);
    }
}

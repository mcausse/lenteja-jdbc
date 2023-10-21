package org.homs.lechuga.queues;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Subscriber {

    public static class EventsConsumer {

        public final String consumerRef;
        public final Class<?> eventTypeToConsume;
        public final Consumer<Event> consumer;

        public EventsConsumer(String consumerRef, Class<?> eventTypeToConsume, Consumer<Event> consumer) {
            this.consumerRef = consumerRef;
            this.eventTypeToConsume = eventTypeToConsume;
            this.consumer = consumer;
        }
    }

    private final EventsRepository eventsRepository;
    private final Timer timer;
    private final Map<Class<?>, List<EventsConsumer>> eventConsumers;

    public Subscriber(EventsRepository eventsRepository, int refreshPeriodMs) {
        this.eventsRepository = eventsRepository;
        this.timer = new Timer(refreshPeriodMs, e -> run());
        this.eventConsumers = new LinkedHashMap<>();
    }

    public void register(EventsConsumer consumer) {
        Class<?> eventType = consumer.eventTypeToConsume;
        if (!eventConsumers.containsKey(eventType)) {
            eventConsumers.put(eventType, new ArrayList<>());
        }
        eventConsumers.get(eventType).add(consumer);
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    protected void run() {
        List<Event> events = eventsRepository.loadEventsFromQueue(eventConsumers.keySet(), EventStatus.PENDING);
        for (var event : events) {
            processEvent(event);
        }
    }

    protected void processEvent(Event event) {
        boolean eventProcessingHasErrors = false;
        eventsRepository.eventChangeStatus(event, EventStatus.PROCESSING);
        if (eventConsumers.containsKey(event.getType())) {
            for (var consumer : eventConsumers.get(event.getType())) {
                try {
                    consumer.consumer.accept(event);
                    // TODO si ok=> borrar possibles errors pq el retry ha funcat
                } catch (Throwable e) {
                    eventProcessingHasErrors = true;
                    eventsRepository.eventChangeStatusToError(event, e, consumer.consumerRef);
                    e.printStackTrace(); // TODO log
                }
            }
        }
        if (!eventProcessingHasErrors) {
            eventsRepository.delete(event);
        }
    }

}

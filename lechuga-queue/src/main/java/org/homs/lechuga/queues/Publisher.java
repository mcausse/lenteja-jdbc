package org.homs.lechuga.queues;

public class Publisher {

    private final EventsRepository eventsRepository;

    public Publisher(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public void publish(Event event) {
        this.eventsRepository.insert(event);
    }

    public void publishAll(Iterable<Event> events) {
        for (var event : events) {
            this.eventsRepository.insert(event);
        }
    }
}

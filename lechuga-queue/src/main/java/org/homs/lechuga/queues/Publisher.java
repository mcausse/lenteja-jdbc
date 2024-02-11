package org.homs.lechuga.queues;

public class Publisher<E extends QueueElement> {

    private final EventsRepository<E> eventsRepository;

    public Publisher(EventsRepository<E> eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public void publish(E queueElement) {
        eventsRepository.getTransactionalOps().run(() -> {
            this.eventsRepository.insert(queueElement);
        });
    }

    public void publishAll(Iterable<E> queueElements) {
        eventsRepository.getTransactionalOps().run(() -> {
            for (var event : queueElements) {
                this.eventsRepository.insert(event);
            }
        });
    }
}

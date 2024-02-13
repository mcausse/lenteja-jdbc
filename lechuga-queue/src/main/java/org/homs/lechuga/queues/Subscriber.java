package org.homs.lechuga.queues;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Subscriber<E extends QueueElement> {

    private final EventsRepository<E> eventsRepository;
    private final Timer timer;
    private final List<Consumer<E>> queueConsumers;

    public Subscriber(EventsRepository<E> eventsRepository, int refreshPeriodMs) {
        this.eventsRepository = eventsRepository;
        this.timer = new Timer(refreshPeriodMs, e -> run());
        this.queueConsumers = new ArrayList<>();
    }

    public void subscribe(Consumer<E> queueConsumer) {
        queueConsumers.add(queueConsumer);
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    protected void run() {
        eventsRepository.getTransactionalOps().run(() -> {
            List<E> queueElements = eventsRepository.loadElementsFromQueue();
            for (var queueElement : queueElements) {
                eventsRepository.delete(queueElement);
                processQueueElement(queueElement);
            }
        });
    }

    protected void processQueueElement(E queueElement) {
        for (var consumer : queueConsumers) {
            try {
                consumer.accept(queueElement);
            } catch (Throwable e) {
                e.printStackTrace(); // TODO log
            }
        }
    }
}

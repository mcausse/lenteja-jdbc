package org.homs.lechuga.queues;

import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PublisherTest {

    final DataAccesFacade facade;
    final EventsRepository<Dog> eventsRepository;

    public PublisherTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:a");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
        this.eventsRepository = new EventsRepository<>(facade, Dog.class);
    }

    @Test
    void publishedEventIsConsumedByTheSubscriber() throws InterruptedException {
        List<Dog> processedEvents = new ArrayList<>();

        var publisher = new Publisher<>(eventsRepository);

        var event = new Dog(123L, "chucho");

        var subscriber = new Subscriber<>(eventsRepository, 500);
        subscriber.subscribe(processedEvents::add);
        subscriber.start();
        Thread.sleep(300L);

        // Act: publish
        publisher.publish(event);

        Thread.sleep(2_000L);
        subscriber.stop();

        // Assert that the event has been removed from the queue
//        EntityManager<Event, String> entityManager = new EntityManagerBuilder(facade).build(Event.class);
//        var eventExistInQueue = TransactionalUtils.runAsReadOnlyWithReturn(facade, () -> entityManager.existsById(event.getUuid()));
//        Assertions.assertThat(eventExistInQueue).isFalse();
        eventsRepository.getTransactionalOps().runAsReadOnly(() -> {
            assertThat(eventsRepository.loadElementsFromQueue()).isEmpty();
        });


        // Assert that the event is being processed by the consumer
        assertThat(processedEvents).hasSize(1);
        // Assert that the event payload is deserialized
        assertThat(processedEvents).hasToString("[Dog{id=123, name='chucho'}]");
    }

}
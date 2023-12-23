package org.homs.lechuga.queues;

import org.assertj.core.api.Assertions;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.TransactionalUtils;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PublisherTest {

    final DataAccesFacade facade;
    final EventsRepository eventsRepository;
    final GsonEvents gsonEvents = new GsonEvents();

    public PublisherTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:a");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
        this.eventsRepository = new EventsRepository(facade);
    }

    @BeforeEach
    public void before() {
        TransactionalUtils.run(facade, () -> {
            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
            sql.runFromClasspath("lechuga-queue.sql");
        });
    }

    @Test
    void publishedEventIsConsumedByTheSubscriber() throws InterruptedException {
        List<Event> processedEvents = new ArrayList<>();

        var publisher = new Publisher(eventsRepository);

        var event = gsonEvents.of(new Dog(123L, "chucho"));

        var subscriber = new Subscriber(this.eventsRepository, 500);
        subscriber.subscribe(Dog.class, processedEvents::add);
        subscriber.subscribe(List.class, processedEvents::add);
        subscriber.start();
        Thread.sleep(300L);

        // Act: publish
        TransactionalUtils.run(facade, () -> {
            publisher.publish(event);
        });

        Thread.sleep(2_000L);
        subscriber.stop();

        // Assert that the event has been removed from the queue
        EntityManager<Event, String> entityManager = new EntityManagerBuilder(facade).build(Event.class);
        var eventExistInQueue = TransactionalUtils.runAsReadOnlyWithReturn(facade, () -> entityManager.existsById(event.getUuid()));
        Assertions.assertThat(eventExistInQueue).isFalse();

        // Assert that the event is being processed by the consumer
        Assertions.assertThat(processedEvents).hasSize(1);
        // Assert that the event payload is deserialized
        Assertions.assertThat(gsonEvents.getPayloadObject(processedEvents.get(0)).toString()).isEqualTo("Dog{id=123, name='chucho'}");
    }

}
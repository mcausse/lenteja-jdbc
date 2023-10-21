package org.homs.lechuga.queues;

import org.assertj.core.api.Assertions;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.queues.util.DateUtil;
import org.homs.lechuga.queues.util.UUIDUtils;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.TransactionalUtils;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PublisherTest {

    final DataAccesFacade facade;

    public PublisherTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:a");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
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

        var publisher = new Publisher(facade);

        var event = new Event();
        event.setUuid(new UUIDUtils().createUUID(String.valueOf(System.currentTimeMillis() + System.nanoTime())));
        event.setType(getClass());
        event.setStatus(EventStatus.PENDING);
        event.setCreated(new Date());

        var subscriber = new Subscriber(facade, new DateUtil());
        subscriber.register(getClass(), processedEvents::add);
        subscriber.register(List.class, processedEvents::add);
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
    }


    @Test
    void publishedEventGeneratesError() throws InterruptedException {

        var publisher = new Publisher(facade);

        var event = new Event();
        event.setUuid(new UUIDUtils().createUUID(String.valueOf(System.currentTimeMillis() + System.nanoTime())));
        event.setType(getClass());
        event.setStatus(EventStatus.PENDING);
        event.setCreated(new Date());

        var subscriber = new Subscriber(facade, new DateUtil());
        subscriber.register(getClass(), (e) -> {
            throw new RuntimeException("jou");
        });
        subscriber.start();
        Thread.sleep(300L);

        // Act: publish
        TransactionalUtils.run(facade, () -> {
            publisher.publish(event);
        });

        Thread.sleep(2_000L);
        subscriber.stop();

        EntityManager<Event, String> entityManager = new EntityManagerBuilder(facade).build(Event.class);

        // Assert that the event has NOT been removed from the queue
        var event2 = TransactionalUtils.runAsReadOnlyWithReturn(facade, () -> entityManager.loadById(event.getUuid()));

        // Assert that the event has status=PROCESSED_WITH_ERROR
        assertThat(event2.getStatus()).isEqualTo(EventStatus.PROCESSED_WITH_ERROR);

        // Assert that the event contains the cause of the error
        assertThat(event2.getErrorMessage()).contains("jou");

        System.out.println(event2);
    }
}
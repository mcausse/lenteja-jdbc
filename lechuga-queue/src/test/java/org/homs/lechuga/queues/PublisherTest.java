package org.homs.lechuga.queues;

import org.assertj.core.api.Assertions;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.queues.util.DateUtil;
import org.homs.lechuga.queues.util.UUIDUtils;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.Transactional;
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
        Transactional.run(facade, () -> {
            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
            sql.runFromClasspath("lechuga-queue.sql");
        });
    }

    @Test
    void name() throws InterruptedException {
        List<Event> processedEvents = new ArrayList<>();

        var publisher = new Publisher(facade);

        var event = new Event();
        event.setUuid(new UUIDUtils().createUUID(String.valueOf(System.currentTimeMillis() + System.nanoTime())));
        event.setType(getClass());
        event.setStatus(EventState.PENDING);
        event.setCreated(new Date());

        var subscriber = new Subscriber(facade, processedEvents::add, List.of(getClass()), new DateUtil());
        subscriber.start();
        Thread.sleep(300L);

        // Act: publish
        Transactional.run(facade, () -> {
            publisher.publish(event);
        });

        Thread.sleep(2_000L);
        subscriber.stop();

        // Assert that the event exists in the queue
        EntityManager<Event, String> entityManager = new EntityManagerBuilder(facade).build(Event.class);
        var eventExistInQueue = Transactional.runReadOnlyWithReturn(facade, () -> entityManager.existsById(event.getUuid()));
        Assertions.assertThat(eventExistInQueue).isTrue();

        // Assert that the event has status=PROCESSED
        var event2 = Transactional.runReadOnlyWithReturn(facade, () -> entityManager.loadById(event.getUuid()));
        assertThat(event2.getStatus()).isEqualTo(EventState.PROCESSED);

        // Assert that the event is being processed by the consumer
        Assertions.assertThat(processedEvents).hasSize(1);
    }
}
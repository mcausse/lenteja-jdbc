package io.homs.votr.messages.application;

import io.homs.votr.messages.domain.Message;
import io.homs.votr.messages.domain.MessageCreatedEvent;
import io.homs.votr.messages.infra.LechugaMessagesRepository;
import org.homs.lechuga.queues.*;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.TransactionalOps;
import org.homs.lentejajdbc.TransactionalUtils;
import org.homs.lentejajdbc.query.QueryObject;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MessagesServiceTest {

    final DataAccesFacade facade;
    final TransactionalOps transactionalOps;
    final EventsRepository eventsRepository;
    final MessagesService messagesService;
    final Subscriber subscriber;

    final List<Event> eventsGenerated = new ArrayList<>();

    final String votrUUID = "1765";
    final String userUUID = "2448";

    public MessagesServiceTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:a");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
        this.transactionalOps = new TransactionalOps(facade);
        this.eventsRepository = new EventsRepository(facade);

        var publisher = new Publisher(new EventsRepository(facade));
        this.subscriber = new Subscriber(new EventsRepository(facade), 250);
        var messagesRepository = new LechugaMessagesRepository(facade, publisher);
        this.messagesService = new MessagesService(transactionalOps, messagesRepository);

        this.eventsGenerated.clear();
    }

    @BeforeEach
    public void before() {
        TransactionalUtils.run(facade, () -> {
            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
            sql.runFromClasspath("lechuga-queue.sql");
            sql.runFromClasspath("votr.sql");

            facade.update(new QueryObject("insert into votrs (uuid,name,descr) values ('" + votrUUID + "','A','B')"));
            facade.update(new QueryObject("insert into users (uuid,email,votr_uuid) values ('" + userUUID + "','A','" + votrUUID + "')"));
        });
        this.subscriber.start();
    }

    @AfterEach
    public void after() {
        this.subscriber.stop();
    }

    @Test
    void createMessage() throws InterruptedException {

        this.subscriber.subscribe(MessageCreatedEvent.class, eventsGenerated::add);
        this.subscriber.subscribe(MessageCreatedEvent.class, System.out::println);


        Message messageToCreate = Message.create(votrUUID, userUUID, "aló world!");

        // Act
        messagesService.createMessage(messageToCreate);

        while (eventsGenerated.isEmpty()) {
            Thread.sleep(50L);
        }

        assertThat(eventsGenerated).hasSize(1);
        assertThat(eventsGenerated.get(0).getType()).isEqualTo(MessageCreatedEvent.class);

        MessageCreatedEvent creationEvent = (MessageCreatedEvent) new GsonEvents().getPayloadObject(eventsGenerated.get(0));
        assertThat(creationEvent.getMessageUUID()).isEqualTo(messageToCreate.getUuid().toString());
    }

    @Test
    void findMessages() throws InterruptedException {

        var messages = messagesService.findMessages(votrUUID, userUUID);
        assertThat(messages).isEmpty();

        createMessage();

        messages = messagesService.findMessages(votrUUID, userUUID);
        assertThat(messages).hasSize(1);

        assertThat(messages.toString()).contains(
                " votrUUID=1765, ",
                " userUUID=2448,",
                " message='aló world!'"
        );
    }
}
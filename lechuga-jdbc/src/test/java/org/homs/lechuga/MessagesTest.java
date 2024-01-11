package org.homs.lechuga;

import org.assertj.core.api.Assertions;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.entity.anno.Enumerated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.repository.LechugaRepository;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.TransactionalOps;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesTest {

    public enum EDirection {IN, OUT}

    @Table("messages")
    public static class Message {
        @Id
        String uuid;
        @Enumerated
        EDirection direction;
        String body;
        String parentUuid;

        public static Message of(EDirection direction, String body, String parentUuid) {
            var r = new Message();
            r.uuid = java.util.UUID.randomUUID().toString();
            r.direction = direction;
            r.body = body;
            r.parentUuid = parentUuid;
            return r;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public EDirection getDirection() {
            return direction;
        }

        public void setDirection(EDirection direction) {
            this.direction = direction;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getParentUuid() {
            return parentUuid;
        }

        public void setParentUuid(String parentUuid) {
            this.parentUuid = parentUuid;
        }
    }

    public static class MessagesRepository extends LechugaRepository<Message, String> {

        public MessagesRepository(EntityManager<Message, String> entityManager) {
            super(entityManager);
        }

        public static class MessageNode {
            public final Message message;
            public final List<MessageNode> childs;

            public MessageNode(Message message, List<MessageNode> childs) {
                this.message = message;
                this.childs = childs;
            }

            @Override
            public String toString() {
                return "{" + message.direction + ":" + message.body +
                        ", childs=" + childs +
                        '}';
            }
        }

        public List<MessageNode> getChildsOf(String messageUuid) {
            return createQuery("m")
                    .append("select {m.*} from {m} where {m.parentUuid=?}", messageUuid)
                    .execute()
                    .load()
                    .stream()
                    .map(m -> new MessageNode(m, getChildsOf(m.uuid)))
                    .collect(Collectors.toList())
                    ;
        }

        public MessageNode loadMessagesTree(String messageUuid) {
            return new MessageNode(
                    findById(messageUuid).get(),
                    getChildsOf(messageUuid)
            );
        }
    }


    final DataAccesFacade facade;
    final TransactionalOps transactionalOps;

    public MessagesTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:a");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
        this.transactionalOps = new TransactionalOps(facade);
    }

    @BeforeEach
    public void before() {
        transactionalOps.run(() -> {
            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
            sql.runFromClasspath("messages.sql");
        });
    }

    @Test
    void name() {
        var messagesEntityManager = new EntityManagerBuilder(facade).<Message, String>build(Message.class);
        var messagesRepo = new MessagesRepository(messagesEntityManager);

        var oml = Message.of(EDirection.OUT, "oml", null);
        var comAck = Message.of(EDirection.IN, "comAck", oml.uuid);
        var appAck = Message.of(EDirection.IN, "appAck", oml.uuid);
        var commAppAck = Message.of(EDirection.OUT, "commAppAck", appAck.uuid);
        transactionalOps.run(() -> {
            messagesRepo.saveAll(List.of(oml, comAck, appAck, commAppAck));
        });

        var r = transactionalOps.runAsReadOnlyWithReturn(() ->
                messagesRepo.loadMessagesTree(oml.uuid)
        );

        Assertions.assertThat(r).hasToString("{OUT:oml, childs=[{IN:comAck, childs=[]}, {IN:appAck, childs=[{OUT:commAppAck, childs=[]}]}]}");
    }
}

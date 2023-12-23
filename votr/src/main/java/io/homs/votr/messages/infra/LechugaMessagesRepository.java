package io.homs.votr.messages.infra;

import io.homs.votr.messages.domain.Message;
import io.homs.votr.messages.domain.MessagesRepository;
import io.homs.votr.shared.UUID;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.entity.query.QueryProcessor;
import org.homs.lechuga.queues.Publisher;
import org.homs.lechuga.repository.LechugaRepository;
import org.homs.lentejajdbc.DataAccesFacade;

import java.util.List;
import java.util.stream.Collectors;

public class LechugaMessagesRepository extends LechugaRepository<LechugaMessage, String> implements MessagesRepository {

    private final Publisher eventsPublisher;

    public LechugaMessagesRepository(DataAccesFacade facade, Publisher eventsPublisher) {
        super(new EntityManagerBuilder(facade).build(LechugaMessage.class));
        this.eventsPublisher = eventsPublisher;
    }

    protected LechugaMessage fromDomain(Message message) {
        var r = new LechugaMessage();
        r.setUuid(message.getUuid().toString());
        r.setVotrUuid(message.getVotrUUID().toString());
        r.setUserUuid(message.getUserUUID().toString());
        r.setPosted(message.getPosted());
        r.setMessage(message.getMessage());
        return r;
    }

    protected Message toDomain(LechugaMessage message) {
        var r = new Message();
        r.setUuid(UUID.of(message.getUuid()));
        r.setVotrUUID(UUID.of(message.getVotrUuid()));
        r.setUserUUID(UUID.of(message.getUserUuid()));
        r.setPosted(message.getPosted());
        r.setMessage(message.getMessage());
        return r;
    }

    @Override
    public void add(Message message) {
        var m = fromDomain(message);
        insert(m);
        eventsPublisher.publishAll(message.getEvents());
    }

    @Override
    public List<Message> find(UUID votrUUID, UUID userUUID) {

        QueryProcessor<LechugaMessage> query = createQuery("m")
                .append("select {m.*} from {m} ")
                .append("where 1=1 ");

        if (votrUUID != null) {
            query.append("and {m.votrUuid=?} ", votrUUID.toString());
        }
        if (votrUUID != null) {
            query.append("and {m.userUuid=?} ", userUUID.toString());
        }
        query.append("order by {m.posted} asc ");

        return query.execute()
                .load()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}

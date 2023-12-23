package io.homs.votr.votr.infra;

import io.homs.votr.shared.UUID;
import io.homs.votr.votr.domain.*;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.queues.GsonEvents;
import org.homs.lechuga.queues.Publisher;
import org.homs.lentejajdbc.DataAccesFacade;


public class LechugaVotrRepository implements VotrRepository {

    private final EntityManager<LechugaVotr, String> votrsEm;
    private final EntityManager<LechugaUser, String> usersEm;
    private final EntityManager<LechugaOption, Integer> optionsEm;

    private final Publisher eventsPublisher;

    public LechugaVotrRepository(DataAccesFacade facade, Publisher eventsPublisher) {
        var emBuilder = new EntityManagerBuilder(facade);
        this.votrsEm = emBuilder.build(LechugaVotr.class);
        this.usersEm = emBuilder.build(LechugaUser.class);
        this.optionsEm = emBuilder.build(LechugaOption.class);
        this.eventsPublisher = eventsPublisher;
    }

    @Override
    public void createVotr(Votr votr) {

        votrsEm.insert(fromDomain(votr));
        eventsPublisher.publish(new GsonEvents().of(new VotrCreatedEvent(votr)));

        for (User user : votr.getUsers()) {
            usersEm.insert(fromDomain(user));
            eventsPublisher.publish(new GsonEvents().of(new UserCreatedEvent(user)));
        }

        for (Option option : votr.getOptions()) {
            optionsEm.insert(fromDomain(votr.getUuid(), option));
        }
    }

    protected LechugaVotr fromDomain(Votr votr) {
        var r = new LechugaVotr();
        r.setUuid(votr.getUuid().toString());
        r.setName(votr.getName());
        r.setDescription(votr.getDescription());
        return r;
    }

    protected LechugaUser fromDomain(User user) {
        var r = new LechugaUser();
        r.setUuid(user.getUuid().toString());
        r.setEmail(user.getEmail());
        r.setAlias(user.getAlias());
        return r;
    }

    protected LechugaOption fromDomain(UUID votrUuid, Option option) {
        var r = new LechugaOption();
        r.setUuid(option.getUuid().toString());
        r.setName(option.getName());
        r.setDescription(option.getDescription());
        r.setVotrUuid(votrUuid.toString());
        return r;
    }
}

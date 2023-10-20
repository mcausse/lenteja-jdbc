package org.homs.lechuga.queues;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.entity.query.QueryProcessor;
import org.homs.lechuga.queues.util.DateUtil;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.ExceptionUtils;
import org.homs.lentejajdbc.Transactional;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public class Subscriber {

    private final DataAccesFacade facade;
    private final EntityManager<Event, String> entityManager;
    private final Consumer<Event> eventsConsumer;
    private final List<Class<?>> subscribedTo;

    private final DateUtil dateUtil;

    private final Timer timer;

    public Subscriber(DataAccesFacade facade, Consumer<Event> eventsConsumer, List<Class<?>> subscribedTo, DateUtil dateUtil) {
        this.facade = facade;
        this.entityManager = new EntityManagerBuilder(facade).build(Event.class);
        this.eventsConsumer = eventsConsumer;
        this.subscribedTo = subscribedTo;
        this.dateUtil = dateUtil;

        this.timer = new Timer(500, e -> run());
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    protected void run() {
        List<Event> events = loadEventsFromQueue();
        for (var event : events) {
            eventChangeStatus(event, EventStatus.PROCESSING);
            try {
                eventsConsumer.accept(event);
                Transactional.run(facade, () -> entityManager.delete(event));
            } catch (Throwable e) {
                eventChangeStatusToError(event, e);
                e.printStackTrace(); // TODO log
            }
        }
    }

    private void eventChangeStatus(Event event, EventStatus state) {
        Transactional.run(facade, () -> {
            event.setStatus(state);
            event.setStatusChanged(dateUtil.now());
            entityManager.update(event);
        });
    }

    private void eventChangeStatusToError(Event event, Throwable e) {
        Transactional.run(facade, () -> {
            event.setStatus(EventStatus.PROCESSED_WITH_ERROR);
            event.setStatusChanged(dateUtil.now());
            event.setErrorMessage(ExceptionUtils.toString(e));
            entityManager.update(event);
        });
    }

    protected List<Event> loadEventsFromQueue() {
        return Transactional.runReadOnlyWithReturn(this.facade, () -> {

            QueryProcessor<Event> q = entityManager.createQuery("e");
            q.append("select {e.*} from {e} where {e.type} in (");
            for (int i = 0; i < subscribedTo.size(); i++) {
                if (i > 0) {
                    q.append(", ");
                }
                Class<?> type = subscribedTo.get(i);
                q.append("{e.type?}", type);
            }
            q.append(") ");
            q.append("and {e.status=?} ", EventStatus.PENDING);
            q.append("order by {e.created} asc ");
            return q.execute().load();
        });
    }
}

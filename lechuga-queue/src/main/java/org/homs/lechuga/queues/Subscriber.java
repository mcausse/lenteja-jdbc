package org.homs.lechuga.queues;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.entity.query.QueryProcessor;
import org.homs.lechuga.queues.util.DateUtil;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.ExceptionUtils;
import org.homs.lentejajdbc.TransactionalOps;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Subscriber {

    private final DataAccesFacade facade;
    private final TransactionalOps transactionalOps;
    private final EntityManager<Event, String> entityManager;
    private final DateUtil dateUtil;
    private final Timer timer;
    private final Map<Class<?>, List<Consumer<Event>>> consumers;

    public Subscriber(DataAccesFacade facade, DateUtil dateUtil, int refreshPeriodMs) {
        this.facade = facade;
        this.transactionalOps = new TransactionalOps(facade);
        this.entityManager = new EntityManagerBuilder(facade).build(Event.class);
        this.dateUtil = dateUtil;
        this.timer = new Timer(refreshPeriodMs, e -> run());
        this.consumers = new LinkedHashMap<>();
    }

    public void register(Class<?> eventType, Consumer<Event> consumer) {
        if (!consumers.containsKey(eventType)) {
            consumers.put(eventType, new ArrayList<>());
        }
        consumers.get(eventType).add(consumer);
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
            processEvent(event);
        }
    }

    protected void processEvent(Event event) {
        eventChangeStatus(event, EventStatus.PROCESSING);
        try {
            if (consumers.containsKey(event.getType())) {
                for (var consumer : consumers.get(event.getType())) {
                    consumer.accept(event);
                }
            }
            transactionalOps.run(() -> entityManager.delete(event));
        } catch (Throwable e) {
            eventChangeStatusToError(event, e);
            e.printStackTrace(); // TODO log
        }
    }

    private void eventChangeStatus(Event event, EventStatus state) {
        transactionalOps.run(() -> {
            event.setStatus(state);
            event.setStatusChanged(dateUtil.now());
            entityManager.update(event);
        });
    }

    private void eventChangeStatusToError(Event event, Throwable e) {
        transactionalOps.run(() -> {
            event.setStatus(EventStatus.PROCESSED_WITH_ERROR);
            event.setStatusChanged(dateUtil.now());
            event.setErrorMessage(ExceptionUtils.toString(e));
            entityManager.update(event);
        });
    }

    protected List<Event> loadEventsFromQueue() {
        return transactionalOps.runAsReadOnlyWithReturn(() -> {

            QueryProcessor<Event> q = entityManager.createQuery("e");
            q.append("select {e.*} from {e} where {e.type} in (");
            final List<Class<?>> consumerTypes = new ArrayList<>(consumers.keySet());
            for (int i = 0; i < consumerTypes.size(); i++) {
                if (i > 0) {
                    q.append(", ");
                }
                Class<?> type = consumerTypes.get(i);
                q.append("{e.type?}", type);
            }
            q.append(") ");
            q.append("and {e.status=?} ", EventStatus.PENDING);
            q.append("order by {e.created} asc ");
            return q.execute().load();
        });
    }
}

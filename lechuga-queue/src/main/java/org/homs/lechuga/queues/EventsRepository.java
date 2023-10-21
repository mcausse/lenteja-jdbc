package org.homs.lechuga.queues;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.entity.query.QueryProcessor;
import org.homs.lechuga.queues.util.DateUtil;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.TransactionalOps;

import java.util.Collection;
import java.util.List;

public class EventsRepository {

    private final EntityManager<Event, String> eventsEntityManager;
    private final EntityManager<EventError, String> eventErrorssEntityManager;

    private final TransactionalOps transactionalOps;
    private final DateUtil dateUtil;

    public EventsRepository(DataAccesFacade facade, DateUtil dateUtil) {
        EntityManagerBuilder entityManagerBuilder = new EntityManagerBuilder(facade);
        this.eventsEntityManager = entityManagerBuilder.build(Event.class);
        this.eventErrorssEntityManager = entityManagerBuilder.build(EventError.class);

        this.transactionalOps = new TransactionalOps(facade);
        this.dateUtil = dateUtil;
    }

    public List<Event> loadEventsFromQueue(Collection<Class<?>> eventTypes, EventStatus status) {
        return transactionalOps.runAsReadOnlyWithReturn(() -> {

            QueryProcessor<Event> q = eventsEntityManager.createQuery("e");
            q.append("select {e.*} from {e} where {e.type} in (");

            int i = 0;
            for (var eventType : eventTypes) {
                if (i > 0) {
                    q.append(", ");
                }
                q.append("{e.type?}", eventType);
                i++;
            }

            q.append(") ");
            q.append("and {e.status=?} ", status);
            q.append("order by {e.created} asc ");
            return q.execute().load();
        });
    }

    public void eventChangeStatus(Event event, EventStatus status) {
        transactionalOps.run(() -> {
            event.setStatus(status);
            event.setStatusChanged(dateUtil.now());
            eventsEntityManager.update(event);
        });
    }

    public void eventChangeStatusToError(Event event, Throwable e, String consumerRef) {
        transactionalOps.run(() -> {
            event.setStatus(EventStatus.PROCESSED_WITH_ERROR);
            event.setStatusChanged(dateUtil.now());
            eventsEntityManager.update(event);

            var eventError = EventError.of(event, consumerRef, e);
            eventErrorssEntityManager.insert(eventError);
        });
    }

    public void delete(Event event) {
        transactionalOps.run(() -> {
            var eventErrors = eventErrorssEntityManager.loadByProperty("eventUuid", event.getUuid());
            eventErrors.forEach(eventErrorssEntityManager::delete);

            eventsEntityManager.delete(event);
        });
    }
}

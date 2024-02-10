package org.homs.lechuga.queues;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.entity.query.QueryProcessor;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.TransactionalOps;

import java.util.Collection;
import java.util.List;

public class EventsRepository {

    private final EntityManager<Event, String> eventsEntityManager;

    private final TransactionalOps transactionalOps;

    public EventsRepository(DataAccesFacade facade) {
        EntityManagerBuilder entityManagerBuilder = new EntityManagerBuilder(facade);
        this.eventsEntityManager = entityManagerBuilder.build(Event.class);
        this.transactionalOps = new TransactionalOps(facade);
    }

    public void insert(Event event) {
        eventsEntityManager.insert(event);
    }

    public List<Event> loadEventsFromQueue(Collection<Class<?>> eventTypes) {
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
            q.append("order by {e.created} asc ");
            return q.execute().load();
        });
    }

    public void delete(Event event) {
        transactionalOps.run(() -> eventsEntityManager.delete(event));
    }
}

package org.homs.lechuga.queues;

import org.homs.lechuga.kv.KvRepository;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.TransactionalOps;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventsRepository<E extends QueueElement> {

    private final KvRepository<E> kvrepository;

    public EventsRepository(DataAccesFacade facade, Class<E> elementClass) {
        var sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        this.kvrepository = new KvRepository<>(facade, elementClass, e -> sdf.format(e.getEnqueuedTime()));
    }

    public void insert(E event) {
        event.setEnqueuedTime(new Date());
        this.kvrepository.store(event);
    }

    public List<E> loadElementsFromQueue() {
        return this.kvrepository.loadAll(KvRepository.SortBy.ASC).stream().map(kv -> kv.value).collect(Collectors.toList());
    }

    public void delete(E event) {
        this.kvrepository.remove(event);
    }

    public TransactionalOps getTransactionalOps() {
        return kvrepository.getTransactionalOps();
    }
}

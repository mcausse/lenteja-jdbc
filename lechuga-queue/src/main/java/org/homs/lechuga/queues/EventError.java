package org.homs.lechuga.queues;

import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lentejajdbc.ExceptionUtils;

import java.util.UUID;

@Table("event_errors")
public class EventError {

    @Id
    String uuid;

    String eventUuid;

    String consumerRef;
    String errorStackTrace;

    public static EventError of(Event event, String consumerRef, Throwable throwable) {
        var r = new EventError();
        r.setUuid(UUID.randomUUID().toString());
        r.setEventUuid(event.getUuid());
        r.setConsumerRef(consumerRef);
        r.setErrorStackTrace(ExceptionUtils.toString(throwable));
        return r;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }

    public String getConsumerRef() {
        return consumerRef;
    }

    public void setConsumerRef(String consumerRef) {
        this.consumerRef = consumerRef;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }
}

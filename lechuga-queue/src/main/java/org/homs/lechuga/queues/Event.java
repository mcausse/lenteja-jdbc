package org.homs.lechuga.queues;

import org.homs.lechuga.entity.anno.Enumerated;
import org.homs.lechuga.entity.anno.Handler;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.handlers.impl.ClassStringHandler;
import org.homs.lechuga.entity.handlers.impl.DateStringHandler;

import java.util.Date;

@Table("events_queue")
public class Event {

    @Id
    String uuid;

    @Handler(value = DateStringHandler.class, args = "yyyyMMdd HH:mm:ss.SSS")
    Date created;

    @Enumerated
    EventStatus status;

    @Handler(value = DateStringHandler.class, args = "yyyyMMdd HH:mm:ss.SSS")
    Date statusChanged;

    @Handler(ClassStringHandler.class)
    Class<?> type;

    String payload;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Date getStatusChanged() {
        return statusChanged;
    }

    public void setStatusChanged(Date statusChanged) {
        this.statusChanged = statusChanged;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Event{" +
                "uuid='" + uuid + '\'' +
                ", created=" + created +
                ", state=" + status +
                ", statusChanged=" + statusChanged +
                ", type=" + type +
                ", payload='" + payload + '\'' +
                '}';
    }
}

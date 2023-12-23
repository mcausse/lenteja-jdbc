package org.homs.lechuga.queues;

import org.homs.lechuga.entity.anno.Handler;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.handlers.impl.ClassStringHandler;
import org.homs.lechuga.entity.handlers.impl.DateStringHandler;

import java.util.Date;

@Table("outbox_table")
public class Event {

    @Id
    String uuid;

    @Handler(value = DateStringHandler.class, args = "yyyyMMdd HH:mm:ss.SSS")
    Date created;

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
                ", type=" + type +
                ", payload='" + payload + '\'' +
                '}';
    }
}

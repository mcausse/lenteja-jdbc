package org.homs.lechuga.queues;

import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

public class GsonEvents {

    private final Gson gson = new Gson();

    public Event of(Object payload) {
        var event = new Event();
        event.setUuid(UUID.randomUUID().toString());
        event.setCreated(new Date());

        event.setType(payload.getClass());
        event.setPayload(gson.toJson(payload));

        return event;
    }

    public Object getPayloadObject(Event event) {
        return gson.fromJson(event.getPayload(), event.getType());
    }
}

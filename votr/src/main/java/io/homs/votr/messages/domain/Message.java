package io.homs.votr.messages.domain;

import io.homs.votr.shared.Entity;
import io.homs.votr.shared.UUID;
import org.homs.lechuga.queues.GsonEvents;

import java.util.Date;

public class Message extends Entity {

    private UUID uuid;

    private UUID votrUUID;
    private UUID userUUID;

    private Date posted;
    private String message;

    public static Message create(String votrUUID, String userUUID, String message) {
        var r = new Message();
        r.uuid = UUID.of();
        r.votrUUID = new UUID(votrUUID);
        r.userUUID = new UUID(userUUID);
        r.posted = new Date();
        r.message = message;

        var createdEvent = new MessageCreatedEvent(r.uuid.toString());
        r.publish(new GsonEvents().of(createdEvent));

        return r;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getVotrUUID() {
        return votrUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public Date getPosted() {
        return posted;
    }

    public String getMessage() {
        return message;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setVotrUUID(UUID votrUUID) {
        this.votrUUID = votrUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uuid=" + uuid +
                ", votrUUID=" + votrUUID +
                ", userUUID=" + userUUID +
                ", posted=" + posted +
                ", message='" + message + '\'' +
                '}';
    }
}
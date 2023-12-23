package io.homs.votr.votr.domain;

import io.homs.votr.shared.Entity;
import io.homs.votr.shared.UUID;
import org.homs.lechuga.queues.GsonEvents;

public class User extends Entity {

    private UUID uuid;
    private String email;
    private String alias;

    public User(UUID uuid, String email, String alias) {
        this.uuid = uuid;
        this.email = email;
        this.alias = alias;
    }

    public static User create(String email) {
        var r = new User(UUID.of(), email, null);
        r.publish(new GsonEvents().of(new UserCreatedEvent(r)));
        return r;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
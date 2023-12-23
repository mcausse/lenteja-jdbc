package io.homs.votr.votr.domain;

import io.homs.votr.shared.Entity;
import io.homs.votr.shared.UUID;
import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.queues.GsonEvents;

import java.util.ArrayList;
import java.util.List;

public class Votr extends Entity {

    private UUID uuid;

    private String name;

    private String description;

    private List<User> users;
    private List<Option> options;

    public static Votr create(String name, String description) {
        var r = new Votr();
        r.setUuid(UUID.of());
        r.setName(name);
        r.setDescription(description);
        r.setUsers(new ArrayList<>());
        r.setOptions(new ArrayList<>());
        r.publish(new GsonEvents().of(new VotrCreatedEvent(r)));
        return r;
    }

    public void createUser(String email) {
        this.users.add(User.create(email));
    }

    public void createOption(Option option) {
        this.options.add(option);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}

package io.homs.votr.votr.domain;

import io.homs.votr.shared.Entity;
import io.homs.votr.shared.UUID;

public class Option extends Entity {

    private UUID uuid;
    private String name;
    private String description;

    public Option(UUID uuid, String name, String description) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
    }

    public static Option create(String name, String description) {
        return new Option(UUID.of(), name, description);
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
}
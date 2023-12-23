package io.homs.votr.shared;

public class UUID {

    private final String uuid;

    public UUID(String uuid) {
        this.uuid = uuid;
    }

    public static UUID of() {
        return new UUID(java.util.UUID.randomUUID().toString());
    }

    public static UUID of(String uuid) {
        return new UUID(uuid);
    }

    public String toString() {
        return uuid;
    }
}

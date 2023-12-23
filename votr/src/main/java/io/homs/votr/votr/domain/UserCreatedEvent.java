package io.homs.votr.votr.domain;

public class UserCreatedEvent {

    private final User user;

    public UserCreatedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

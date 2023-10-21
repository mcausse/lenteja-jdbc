package trash.domain.entity;

import java.util.Date;

public class Message {

    private final Date timestamp;
    private final User user;
    private final String message;

    public Message(Date timestamp, User user, String message) {
        this.timestamp = timestamp;
        this.user = user;
        this.message = message;
    }

    public static Message create(Date timestamp, User user, String message) {
        return new Message(timestamp, user, message);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
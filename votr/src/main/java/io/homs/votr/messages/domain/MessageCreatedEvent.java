package io.homs.votr.messages.domain;

public class MessageCreatedEvent {

    private final String messageUUID;

    public MessageCreatedEvent(String messageUUID) {
        this.messageUUID = messageUUID;
    }

    public String getMessageUUID() {
        return messageUUID;
    }
}

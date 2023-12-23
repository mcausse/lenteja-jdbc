package io.homs.votr.messages.domain;

import io.homs.votr.shared.UUID;

import java.util.List;

public interface MessagesRepository {

    void add(Message message);

    List<Message> find(UUID votrUUID, UUID userUUID);
}

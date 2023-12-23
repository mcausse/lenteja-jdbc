package io.homs.votr.messages.application;

import io.homs.votr.messages.domain.Message;
import io.homs.votr.messages.domain.MessagesRepository;
import io.homs.votr.shared.UUID;
import org.homs.lentejajdbc.TransactionalOps;

import java.util.List;

public class MessagesService {

    private final TransactionalOps transactionalOps;
    private final MessagesRepository messagesRepository;

    public MessagesService(TransactionalOps transactionalOps, MessagesRepository messagesRepository) {
        this.transactionalOps = transactionalOps;
        this.messagesRepository = messagesRepository;
    }

    public void createMessage(Message message) {
        transactionalOps.run(() -> messagesRepository.add(message));
    }

    public List<Message> findMessages(String votrUUID, String userUUID) {
        return transactionalOps.runAsReadOnlyWithReturn(() -> messagesRepository.find(
                UUID.of(votrUUID),
                UUID.of(userUUID)
        ));
    }
}

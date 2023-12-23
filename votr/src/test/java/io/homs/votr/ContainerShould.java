package io.homs.votr;

import io.homs.votr.Container;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class ContainerShould {

    public interface IMessage {
    }

    public static class Message implements IMessage {
        @Override
        public String toString() {
            return "hello world";
        }
    }

    @Test
    void instantiate_a_simple_class() {
        var c = new Container();

        // Act
        Message message = c.resolve(Message.class);

        assertThat(message).hasToString("hello world");
    }

    @Test
    void fail_when_instantiating_a_class_whithout_registering_its_base_type() {
        var c = new Container();
        // c.register(IMessage.class, Message.class);

        try {
            // Act
            IMessage message = c.resolve(IMessage.class);

            fail("");
        } catch (Exception e) {
            assertThat(e).getCause().hasMessage("this interface needs to be registered: io.homs.votr.ContainerShould$IMessage");
        }
    }

    @Test
    void instantiate_a_simple_class_by_its_base_type() {
        var c = new Container();
        c.register(IMessage.class, Message.class);

        // Act
        IMessage message = c.resolve(IMessage.class);

        assertThat(message).hasToString("hello world");
    }

    public static class MessageProvider {
        final IMessage message;

        public MessageProvider(IMessage message) {
            this.message = message;
        }

        public IMessage getMessage() {
            return message;
        }
    }

    @Test
    void build_a_class_injecting_its_dependencies_resolved() {
        var c = new Container();
        c.register(IMessage.class, Message.class);

        // Act
        MessageProvider messageProvider = c.resolve(MessageProvider.class);

        assertThat(messageProvider).isNotNull();
        assertThat(messageProvider.getMessage()).hasToString("hello world");
    }
}
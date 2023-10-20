package io.homs.votr.domain;

import java.util.List;

public class Votr {

    String uuid;

    String name;
    String description;

    List<User> users;
    List<Option> options;

    Messages messages;

    public void addMessage(Message message) {

    }

    public static class Messages {
        List<Message> messages;
    }

    public static class Message {
        String uuid;
        User user;
        String message;
    }

    public static class User {
        String uuid;
        String email;
        String alias;
        OptionsVoted optionsVoted;
    }

    public static class OptionsVoted {
        List<Option> optionsVoted;
    }

    public static class Option {
        Integer id;
        String name;
        String description;
    }
}

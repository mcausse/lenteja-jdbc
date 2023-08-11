package trash.domain.entity;

import trash.utils.UUIDUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Votr {

    private final String uuid;

    private String name;
    private String description;

    private final Date creationDate;
    private final User creationUser;
    private Date expirationDate;

    private final List<Option> options;
    private final List<Message> messages;
    private final List<User> users;

    public Votr(String uuid, Date creationDate, User creationUser) {
        this.uuid = uuid;
        this.creationDate = creationDate;
        this.creationUser = creationUser;
        this.options = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public Votr(String uuid, String name, String description,
                Date creationDate, User creationUser, Date expirationDate,
                List<Option> options,
                List<Message> messages,
                List<User> users) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.creationUser = creationUser;
        this.expirationDate = expirationDate;
        this.options = options;
        this.messages = messages;
        this.users = users;
    }

    public static Votr createNewVotr(String name, String description, Date creationDate, String emailCreationUser, Date expirationDate) {
        String votrUuid = UUIDUtils.createUUID(name, description, emailCreationUser);
        User creationUser = User.create(votrUuid, emailCreationUser);

        return new Votr(
                votrUuid,
                name,
                description,
                creationDate,
                creationUser,
                expirationDate,
                new ArrayList<>(),
                new ArrayList<>(List.of(Message.create(creationDate, creationUser, "votr created"))),
                new ArrayList<>(List.of(creationUser))
        );
    }

    public void addUser(String email) {
        this.users.add(User.create(getUuid(), email));
        this.messages.add(Message.create(new Date(), getCreationUser(), "user added: " + email));
    }

    public void addOption(String name, String description) {
        this.options.add(
                new Option(
                        UUID.nameUUIDFromBytes((uuid + name + description).getBytes()).toString(),
                        name,
                        description));
        this.messages.add(Message.create(new Date(), getCreationUser(), "option added: " + name));
    }

    public String getUuid() {
        return uuid;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Option> getOptions() {
        return options;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getCreationUser() {
        return creationUser;
    }

    @Override
    public String toString() {
        return "Votr{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + (creationDate == null ? "null" : new SimpleDateFormat("yyyyMMdd").format(creationDate)) +
                ", creationUser=" + creationUser +
                ", expirationDate=" + (expirationDate == null ? "null" : new SimpleDateFormat("yyyyMMdd").format(expirationDate)) +
                ", options=" + options +
//                ", messages=" + messages +
                ", users=" + users +
                '}';
    }
}

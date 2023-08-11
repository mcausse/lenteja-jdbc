package trash.domain.entity;

import trash.utils.UUIDUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User {

    private final String uuid;

    private final String email;
    private String alias;

    private final List<OptionVoted> optionsVoted;

    public User(String uuid, String email) {
        this.uuid = uuid;
        this.email = email;
        this.optionsVoted = new ArrayList<>();
    }

    public static User create(String hashVotr, String emailCreationUser) {
        return new User(
                UUIDUtils.createUUID(hashVotr, emailCreationUser),
                emailCreationUser
        );
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void addOptionVoted(Option option) {
        this.optionsVoted.add(new OptionVoted(option, new Date()));
    }

    public void removeOptionVoted(Option option) {
        this.optionsVoted.remove(new OptionVoted(option, new Date()));
    }

    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getAlias() {
        return alias;
    }

    public List<OptionVoted> getOptionsVoted() {
        return optionsVoted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", email='" + email + '\'' +
                ", alias='" + alias + '\'' +
                ", optionsVoted=" + optionsVoted +
                '}';
    }
}

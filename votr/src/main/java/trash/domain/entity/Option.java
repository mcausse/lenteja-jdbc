package trash.domain.entity;

import java.util.Objects;

public class Option {

    private final String uuid;

    private final String name;
    private final String description;

    public Option(String uuid, String name, String description) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return uuid.equals(option.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}

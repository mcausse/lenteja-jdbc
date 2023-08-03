package org.homs.votr.domain.entity;

import java.util.Date;
import java.util.Objects;

public class OptionVoted {

    final Option option;
    final Date timestampVoted;

    public OptionVoted(Option option, Date timestampVoted) {
        this.option = option;
        this.timestampVoted = timestampVoted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionVoted that = (OptionVoted) o;
        return option.equals(that.option);
    }

    @Override
    public int hashCode() {
        return Objects.hash(option);
    }
}

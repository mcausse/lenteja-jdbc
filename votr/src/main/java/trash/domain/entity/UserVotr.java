package trash.domain.entity;

public class UserVotr {

    public final Votr votr;
    public final User user;

    public UserVotr(Votr votr, User user) {
        this.votr = votr;
        this.user = user;
    }
}

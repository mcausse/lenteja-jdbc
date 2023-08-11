package trash.domain.repository;

import trash.domain.entity.User;

public interface UserRepository {

    User load(String hashUser);
}

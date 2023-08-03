package org.homs.votr.domain.repository;

import org.homs.votr.domain.entity.User;

public interface UserRepository {

    User load(String hashUser);

    void create(User user);
}

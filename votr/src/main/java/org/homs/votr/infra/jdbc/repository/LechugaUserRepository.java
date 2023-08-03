package org.homs.votr.infra.jdbc.repository;

import org.homs.lechuga.entity.EntityManager;
import org.homs.votr.domain.entity.User;
import org.homs.votr.domain.repository.UserRepository;
import org.homs.votr.infra.jdbc.entity.LechugaUser;

import java.util.List;
import java.util.stream.Collectors;

public class LechugaUserRepository implements UserRepository {

    final EntityManager<LechugaUser, Integer> entityManager;

    public LechugaUserRepository(EntityManager<LechugaUser, Integer> entityManager) {
        this.entityManager = entityManager;
    }

    public User load(String hashUser) {
        LechugaUser lechugaUser = entityManager
                .createQuery("u")
                .append("select {u.*} from {u} where {u.userHash=?} ", hashUser)
                .execute()
                .loadUnique();

        return toDomain(lechugaUser);
    }

    @Override
    public void create(User user) {
        LechugaUser lechugaUser = fromDomain(user);
        entityManager.store(lechugaUser);
    }

    public void create(User u, int votrId) {
        LechugaUser lechugaUser = fromDomain(u);
        lechugaUser.setVotrId(votrId);
        entityManager.store(lechugaUser);
    }

    public List<User> loadByVotrId(int votrId) {
        List<LechugaUser> lechugaUsers = entityManager
                .createQuery("u")
                .append("select {u.*} from {u} where {u.votrId=?} ", votrId)
                .execute()
                .load();

        return lechugaUsers.stream().map(this::toDomain).collect(Collectors.toList());
    }

    protected LechugaUser fromDomain(User user) {
        var r = new LechugaUser();
        r.setUserHash(user.getUuid());
        r.setEmail(user.getEmail());
        r.setAlias(user.getAlias());
        return r;
    }

    protected User toDomain(LechugaUser lechugaUser) {
        return new User(
                lechugaUser.getUserHash(),
                lechugaUser.getEmail()
        );
    }

}

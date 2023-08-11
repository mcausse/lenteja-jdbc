package trash.infra.jdbc.repository;

import org.homs.lechuga.entity.EntityManager;
import trash.infra.jdbc.entity.LechugaUser;

import java.util.List;

public class LechugaUserRepository {

    final EntityManager<LechugaUser, Integer> entityManager;

    public LechugaUserRepository(EntityManager<LechugaUser, Integer> entityManager) {
        this.entityManager = entityManager;
    }

    public LechugaUser load(String hashUser) {
        return entityManager
                .createQuery("u")
                .append("select {u.*} from {u} where {u.userHash=?} ", hashUser)
                .execute()
                .loadUnique();
    }

    public void create(LechugaUser lechugaUser) {
        entityManager.store(lechugaUser);
    }

    public List<LechugaUser> loadByVotrId(int votrId) {
        return entityManager
                .createQuery("u")
                .append("select {u.*} from {u} where {u.votrId=?} ", votrId)
                .execute()
                .load();
    }
}

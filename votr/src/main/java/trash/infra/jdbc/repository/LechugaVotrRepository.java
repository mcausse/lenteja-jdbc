package trash.infra.jdbc.repository;

import org.homs.lechuga.entity.EntityManager;
import trash.infra.jdbc.entity.LechugaVotr;

public class LechugaVotrRepository {

    final EntityManager<LechugaVotr, Integer> entityManager;
    final LechugaUserRepository lechugaUserRepository;

    public LechugaVotrRepository(EntityManager<LechugaVotr, Integer> entityManager, LechugaUserRepository lechugaUserRepository) {
        this.entityManager = entityManager;
        this.lechugaUserRepository = lechugaUserRepository;
    }

    public LechugaVotr load(String hashVotr) {
        return entityManager
                .createQuery("v")
                .append("select {v.*} from {v} where {v.votrHash=?} ", hashVotr)
                .execute()
                .loadUnique();
    }

    public void create(LechugaVotr lechugaVotr) {
        entityManager.store(lechugaVotr);
    }
}

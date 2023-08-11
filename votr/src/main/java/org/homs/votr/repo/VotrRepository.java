package org.homs.votr.repo;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.repository.LechugaRepository;
import org.homs.votr.ent.Votr;

public class VotrRepository extends LechugaRepository<Votr, Integer> {

    public VotrRepository(EntityManager<Votr, Integer> entityManager) {
        super(entityManager);
    }

    public void create(Votr votr) {
        getEntityManager().store(votr);
    }

    public Votr loadByHash(String hashVotr) {
        return getEntityManager()
                .createQuery("v")
                .append("select {v.*} from {v} where {v.votrHash=?} ", hashVotr)
                .execute()
                .loadUnique();
    }
}

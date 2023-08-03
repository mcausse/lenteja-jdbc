package org.homs.votr.infra.jdbc.repository;

import org.homs.lechuga.entity.EntityManager;
import org.homs.votr.domain.entity.User;
import org.homs.votr.domain.entity.Votr;
import org.homs.votr.domain.repository.VotrRepository;
import org.homs.votr.infra.jdbc.entity.LechugaVotr;

import java.util.List;

public class LechugaVotrRepository implements VotrRepository {

    final EntityManager<LechugaVotr, Integer> entityManager;
    final LechugaUserRepository lechugaUserRepository;

    public LechugaVotrRepository(EntityManager<LechugaVotr, Integer> entityManager, LechugaUserRepository lechugaUserRepository) {
        this.entityManager = entityManager;
        this.lechugaUserRepository = lechugaUserRepository;
    }

    public Votr load(String hashVotr) {
        var lechugaVotr = entityManager
                .createQuery("v")
                .append("select {v.*} from {v} where {v.votrHash=?} ", hashVotr)
                .execute()
                .loadUnique();

        List<User> users = lechugaUserRepository.loadByVotrId(lechugaVotr.getVotrId());
        User creationUser = lechugaUserRepository.load(lechugaVotr.getCreationUserHash());
        var r= toDomain(lechugaVotr, creationUser);
        r.getUsers().addAll(users);
        return r;
    }

    public void create(Votr votr) {
        LechugaVotr lechugaVotr = fromDomain(votr);
        entityManager.store(lechugaVotr);

        votr.getUsers().forEach(u -> lechugaUserRepository.create(u, lechugaVotr.getVotrId()));
    }

    protected LechugaVotr fromDomain(Votr votr) {
        var r = new LechugaVotr();
        r.setVotrHash(votr.getUuid());
        r.setTitle(votr.getName());
        r.setDescription(votr.getDescription());
        r.setCreationDate(votr.getCreationDate());
        r.setCreationUserHash(votr.getCreationUser().getUuid());
        return r;
    }

    protected Votr toDomain(LechugaVotr lechugaVotr, User creationUser) {
        var r= new Votr(
                lechugaVotr.getVotrHash(),
                lechugaVotr.getCreationDate(),
                creationUser
        );

        r.setName(lechugaVotr.getTitle());
        r.setDescription(lechugaVotr.getDescription());
        return r;
    }

}

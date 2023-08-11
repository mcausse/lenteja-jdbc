package org.homs.votr.repo;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.repository.LechugaRepository;
import org.homs.votr.ent.User;

import java.util.List;

public class UserRepository extends LechugaRepository<User, Integer> {

    public UserRepository(EntityManager<User, Integer> entityManager) {
        super(entityManager);
    }

    public User load(int votrId, String hashUser) {
        return getEntityManager()
                .createQuery("u")
                .append("select {u.*} from {u} where {u.userHash=?} and {u.votrId=?}", hashUser, votrId)
                .execute()
                .loadUnique();
    }

    public List<User> loadByVotrId(int votrId) {
        return getEntityManager()
                .createQuery("u")
                .append("select {u.*} from {u} where {u.votrId=?}", votrId)
                .execute()
                .load();
    }

//
//    public void create(User lechugaUser) {
//        entityManager.store(lechugaUser);
//    }
//
//    public List<User> loadByVotrId(int votrId) {
//        return entityManager
//                .createQuery("u")
//                .append("select {u.*} from {u} where {u.votrId=?} ", votrId)
//                .execute()
//                .load();
//    }
}

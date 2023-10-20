package org.homs.votr.repo;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.repository.LechugaRepository;
import org.homs.votr.ent.Message;

import java.util.List;

public class MessageRepository extends LechugaRepository<Message, Long> {

    public MessageRepository(EntityManager<Message, Long> entityManager) {
        super(entityManager);
    }

    public List<Message> load(int votrId) {
        return getEntityManager()
                .createQuery("m")
                .append("select {m.*} from {m} where {m.votrId=?} ", votrId)
                .append("order by {m.timestamp} asc")
                .execute()
                .load();
    }
}

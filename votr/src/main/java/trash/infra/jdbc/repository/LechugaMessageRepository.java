package trash.infra.jdbc.repository;

import org.homs.lechuga.entity.EntityManager;
import trash.infra.jdbc.entity.LechugaMessage;

import java.util.List;

public class LechugaMessageRepository {

    final EntityManager<LechugaMessage, Long> entityManager;

    public LechugaMessageRepository(EntityManager<LechugaMessage, Long> entityManager) {
        this.entityManager = entityManager;
    }

    public List<LechugaMessage> load(String votrId) {
        return entityManager
                .createQuery("m")
                .append("select {m.*} from {m} where {m.votrId=?} ", votrId)
                .append("order by {m.timestamp} asc")
                .execute()
                .load();
    }

    public void create(LechugaMessage lechugaMessage) {
        entityManager.store(lechugaMessage);
    }
}

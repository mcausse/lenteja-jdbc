package org.homs.votr.repo;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.repository.LechugaRepository;
import org.homs.lentejajdbc.ScalarMappers;
import org.homs.votr.ent.Option;
import org.homs.votr.ent.OptionId;

import java.util.List;

public class OptionRepository extends LechugaRepository<Option, OptionId> {

    public OptionRepository(EntityManager<Option, OptionId> entityManager) {
        super(entityManager);
    }

    public List<Option> load(int votrId) {
        return getEntityManager()
                .createQuery("o")
                .append("select {o.*} from {o} where {o.optionId.votrId=?} ", votrId)
                .execute()
                .load();
    }

    public int getNextNumOrder() {
        Integer r = getEntityManager()
                .createQuery("o")
                .append("select max({o.optionId.numOrder}) from {o}")
                .execute()
                .loadUniqueScalar(ScalarMappers.INTEGER);
        if (r == null) {
            return 0;
        }
        return r + 1;
    }


//    public void create(Option option) {
//        getEntityManager().store(option);
//    }
}

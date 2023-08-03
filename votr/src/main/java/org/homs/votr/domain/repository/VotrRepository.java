package org.homs.votr.domain.repository;

import org.homs.votr.domain.entity.Votr;

public interface VotrRepository {

    Votr load(String hashVotr);

    void create(Votr votr);
}

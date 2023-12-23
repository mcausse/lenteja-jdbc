package io.homs.votr.votr.application;

import io.homs.votr.votr.domain.Votr;
import io.homs.votr.votr.domain.VotrRepository;
import org.homs.lentejajdbc.TransactionalOps;

public class VotrService {

    private final TransactionalOps transactionalOps;
    private final VotrRepository votrRepository;

    public VotrService(TransactionalOps transactionalOps, VotrRepository votrRepository) {
        this.transactionalOps = transactionalOps;
        this.votrRepository = votrRepository;
    }

    public void createVotr(Votr votr) {
        transactionalOps.run(() -> votrRepository.createVotr(votr));
    }
}

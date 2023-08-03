package org.homs.votr.domain.service;

import org.homs.votr.domain.entity.UserVotr;
import org.homs.votr.domain.entity.Votr;
import org.homs.votr.domain.repository.UserRepository;
import org.homs.votr.domain.repository.VotrRepository;

public class VotrService {

    final VotrRepository votrRepository;
    final UserRepository userRepository;

    public VotrService(VotrRepository votrRepository, UserRepository userRepository) {
        this.votrRepository = votrRepository;
        this.userRepository = userRepository;
    }

    public UserVotr load(String hashVotr, String hashUser) {
        return new UserVotr(
                votrRepository.load(hashVotr),
                userRepository.load(hashUser)
        );
    }

    public void create(Votr votr) {
        votrRepository.create(votr);
    }
}

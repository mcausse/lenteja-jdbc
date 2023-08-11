package trash.domain.service;

import trash.domain.entity.UserVotr;
import trash.domain.entity.Votr;
import trash.domain.repository.UserRepository;
import trash.domain.repository.VotrRepository;

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

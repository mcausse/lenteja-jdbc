package trash.domain.repository;

import trash.domain.entity.Votr;

public interface VotrRepository {

    Votr load(String hashVotr);

    void create(Votr votr);
}

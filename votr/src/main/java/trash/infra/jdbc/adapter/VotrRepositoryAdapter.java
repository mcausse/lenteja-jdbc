package trash.infra.jdbc.adapter;

import trash.domain.entity.User;
import trash.domain.entity.Votr;
import trash.domain.repository.VotrRepository;
import trash.infra.jdbc.entity.LechugaVotr;
import trash.infra.jdbc.repository.LechugaVotrRepository;

import java.util.List;

public class VotrRepositoryAdapter implements VotrRepository {

    final LechugaVotrRepository lechugaVotrRepository;
    final UserRepositoryAdapter userRepositoryAdapter;

    public VotrRepositoryAdapter(LechugaVotrRepository lechugaVotrRepository, UserRepositoryAdapter userRepositoryAdapter) {
        this.lechugaVotrRepository = lechugaVotrRepository;
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    @Override
    public Votr load(String hashVotr) {
        LechugaVotr lechugaVotr = lechugaVotrRepository.load(hashVotr);
        User creationUser = userRepositoryAdapter.load(lechugaVotr.getCreationUserHash());
        Votr votr = toDomain(lechugaVotr, creationUser);

        List<User> users = userRepositoryAdapter.loadByVotrId(lechugaVotr.getVotrId());
        votr.getUsers().addAll(users);
        return votr;
    }

    @Override
    public void create(Votr votr) {
        LechugaVotr lechugaVotr = fromDomain(votr);
        lechugaVotrRepository.create(lechugaVotr);

        int idVotr = lechugaVotr.getVotrId();
        votr.getUsers().forEach(u -> userRepositoryAdapter.create(idVotr, u));
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
        var r = new Votr(
                lechugaVotr.getVotrHash(),
                lechugaVotr.getCreationDate(),
                creationUser
        );

        r.setName(lechugaVotr.getTitle());
        r.setDescription(lechugaVotr.getDescription());
        return r;
    }
}

package trash.infra.jdbc.adapter;

import trash.domain.entity.User;
import trash.domain.repository.UserRepository;
import trash.infra.jdbc.entity.LechugaUser;
import trash.infra.jdbc.repository.LechugaUserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryAdapter implements UserRepository {

    final LechugaUserRepository lechugaUserRepository;

    public UserRepositoryAdapter(LechugaUserRepository lechugaUserRepository) {
        this.lechugaUserRepository = lechugaUserRepository;
    }

    @Override
    public User load(String hashUser) {
        return toDomain(lechugaUserRepository.load(hashUser));
    }

    public List<User> loadByVotrId(int votrId) {
        return lechugaUserRepository.loadByVotrId(votrId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    public void create(int idVotr, User u) {
        LechugaUser lechugaUser = fromDomain(u);
        lechugaUser.setVotrId(idVotr);
        lechugaUserRepository.create(lechugaUser);
    }

    protected LechugaUser fromDomain(User user) {
        var r = new LechugaUser();
        r.setUserHash(user.getUuid());
        r.setEmail(user.getEmail());
        r.setAlias(user.getAlias());
        return r;
    }

    protected User toDomain(LechugaUser lechugaUser) {
        return new User(
                lechugaUser.getUserHash(),
                lechugaUser.getEmail()
        );
    }
}

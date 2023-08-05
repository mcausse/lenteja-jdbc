package org.homs.votr;

import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.homs.votr.domain.entity.UserVotr;
import org.homs.votr.domain.entity.Votr;
import org.homs.votr.domain.service.VotrService;
import org.homs.votr.infra.jdbc.adapter.UserRepositoryAdapter;
import org.homs.votr.infra.jdbc.adapter.VotrRepositoryAdapter;
import org.homs.votr.infra.jdbc.entity.LechugaUser;
import org.homs.votr.infra.jdbc.entity.LechugaVotr;
import org.homs.votr.infra.jdbc.repository.LechugaUserRepository;
import org.homs.votr.infra.jdbc.repository.LechugaVotrRepository;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class VotrServiceShould {

    final SdfUtil sdfUtil = new SdfUtil();
    final DataAccesFacade facade;

    public VotrServiceShould() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:a");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
    }

    @BeforeEach
    public void before() {
        facade.begin();
        try {
            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
            sql.runFromClasspath("votr.sql");
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw e;
        }
    }

    private VotrService buildVotrService() {
        var entityManagerBuilder = new EntityManagerBuilder(facade);
        var userRepo = new LechugaUserRepository(entityManagerBuilder.build(LechugaUser.class));
        var votrRepo = new LechugaVotrRepository(entityManagerBuilder.build(LechugaVotr.class), userRepo);

        UserRepositoryAdapter userRepositoryAdapter = new UserRepositoryAdapter(userRepo);
        VotrRepositoryAdapter votrRepositoryAdapter = new VotrRepositoryAdapter(votrRepo, userRepositoryAdapter);
        return new VotrService(votrRepositoryAdapter, userRepositoryAdapter);
    }

    private LechugaVotr loadVotr(String uuid) {
        var entityManagerBuilder = new EntityManagerBuilder(facade);
        var em = entityManagerBuilder.build(LechugaVotr.class);

        facade.begin();
        try {
            return em.createQuery("v")
                    .append("select {v.*} from {v} where {v.votrHash=?} ", uuid)
                    .execute()
                    .loadUnique();
        } finally {
            facade.rollback();
        }
    }

    private List<LechugaUser> loadUsers(int votrId) {
        var entityManagerBuilder = new EntityManagerBuilder(facade);
        var em = entityManagerBuilder.build(LechugaUser.class);

        facade.begin();
        try {
            return em
                    .createQuery("u")
                    .append("select {u.*} from {u} where {u.votrId=?} ", votrId)
                    .execute()
                    .load();
        } finally {
            facade.rollback();
        }
    }

    @Test
    void store_properly_a_simple_votr_with_2_users() {
        VotrService service = buildVotrService();

        var votr = Votr.create("best poem", "best poem of history", sdfUtil.toDate("19820522 01:02:03"), "mhc@votr.com", null);
        votr.addUser("lmm@votr.com");
        var votrUUID = votr.getUuid();

        // Act
        facade.begin();
        try {
            service.create(votr);
        } finally {
            facade.commit();
        }

        LechugaVotr storedVotr = loadVotr(votrUUID);
        List<LechugaUser> votrUsers = loadUsers(storedVotr.getVotrId());

        assertThat(votrUsers).hasSize(2);
        assertThat(storedVotr).hasToString(
                "LechugaVotr{votrId=100, votrHash='50cb17e5-e723-3169-82f1-7bc67644d9e4', " +
                        "title='best poem', " +
                        "description='best poem of history', " +
                        "creationDate=1982-05-22 01:02:03.0, " +
                        "creationUserHash='2783c258-92a0-36d1-8fd8-0abcdb42774b'}"
        );
        assertThat(votrUsers).hasToString(
                "[LechugaUser{userId=101, userHash='2783c258-92a0-36d1-8fd8-0abcdb42774b', email='mhc@votr.com', alias='null', votrId=100, votedOptionNumOrder='null', votedOptionDate='null'}, " +
                        "LechugaUser{userId=102, userHash='fc49896e-dcba-3f61-9b40-81f252743eb0', email='lmm@votr.com', alias='null', votrId=100, votedOptionNumOrder='null', votedOptionDate='null'}]"
        );
    }

    @Test
    void load_a_previously_created_votr_with_2_users() {
        VotrService service = buildVotrService();

        var votr = Votr.create("best poem", "best poem of history", sdfUtil.toDate("19820522 01:02:03"), "mhc@votr.com", null);
        votr.addUser("lmm@votr.com");

        facade.begin();
        try {
            service.create(votr);
        } finally {
            facade.commit();
        }

        String uuidVotr = votr.getUuid();
        String uuidLmm = votr.getUsers().stream().filter(u -> u.getEmail().equals("lmm@votr.com")).collect(Collectors.toList()).get(0).getUuid();

        // Act
        UserVotr loadedVotr;
        facade.begin();
        try {
            loadedVotr = service.load(uuidVotr, uuidLmm);
        } finally {
            facade.rollback();
        }

        assertThat(loadedVotr.user.getUuid()).isEqualTo(uuidLmm);
        assertThat(loadedVotr.user.getEmail()).isEqualTo("lmm@votr.com");
        assertThat(loadedVotr.votr.getUsers()).hasSize(2);
        assertThat(loadedVotr.votr).hasToString(votr.toString());
    }

}

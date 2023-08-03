package org.homs.votr;

import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.homs.votr.domain.entity.UserVotr;
import org.homs.votr.domain.entity.Votr;
import org.homs.votr.domain.service.VotrService;
import org.homs.votr.infra.jdbc.entity.LechugaUser;
import org.homs.votr.infra.jdbc.entity.LechugaVotr;
import org.homs.votr.infra.jdbc.repository.LechugaUserRepository;
import org.homs.votr.infra.jdbc.repository.LechugaVotrRepository;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceTest {

    final DataAccesFacade facade;

    public ServiceTest() {
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
        return new VotrService(votrRepo, userRepo);
    }

    @Test
    void create() {
        VotrService service = buildVotrService();

        var votr = Votr.create("best poem", "best poem of history", "mhc@votr.com", null);
        votr.addUser("lmm@votr.com");

        // Act
        facade.begin();
        try {
            service.create(votr);
        } finally {
            facade.commit();
        }
    }

    @Test
    void load() {
        VotrService service = buildVotrService();

        var votr = Votr.create("best poem", "best poem of history", "mhc@votr.com", null);
        votr.addUser("lmm@votr.com");

        // Act
        facade.begin();
        try {
            service.create(votr);
        } finally {
            facade.commit();
        }


        String uuidVotr = votr.getUuid();
        String uuidLmm = votr.getUsers().stream().filter(u -> u.getEmail().equals("lmm@votr.com")).collect(Collectors.toList()).get(0).getUuid();

        UserVotr loadedVotr;
        facade.begin();
        try {
            loadedVotr = service.load(uuidVotr, uuidLmm);
        } finally {
            facade.rollback();
        }

        assertThat(loadedVotr.votr).hasToString(votr.toString());
    }

}

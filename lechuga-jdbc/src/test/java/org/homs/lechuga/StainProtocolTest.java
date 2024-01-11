package org.homs.lechuga;

import lombok.Data;
import lombok.Getter;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lechuga.entity.anno.*;
import org.homs.lechuga.entity.generator.impl.HsqldbIdentity;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.ScalarMappers;
import org.homs.lentejajdbc.TransactionalOps;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StainProtocolTest {

    @Data
    @Table("stain_protocol")
    public static class StainProtocol {

        @Id
        @Generated(HsqldbIdentity.class)
        Long id;

        String name;

//        public Long getId() {
//            return id;
//        }
//
//        public void setId(Long id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
    }

    @Getter
    @Data
    @Table("adv_stain_protocol")
    public static class AdvancedStainProtocol {

        @Data
        public static class AdvancedStainProtocolId {

            String host;

            @Column("id_prot")
            Long idStain;
        }

        @Id
        @Embedded
        AdvancedStainProtocolId id;

        public void setId(AdvancedStainProtocolId id) {
            this.id = id;
        }
    }

    final DataAccesFacade facade;
    final TransactionalOps transactionalOps;

    public StainProtocolTest() {
        final JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:aa");
        ds.setUser("sa");
        ds.setPassword("");
        this.facade = new JdbcDataAccesFacade(ds);
        this.transactionalOps = new TransactionalOps(facade);
    }

    @BeforeEach
    public void before() {
        transactionalOps.run(() -> {
            SqlScriptExecutor sql = new SqlScriptExecutor(facade);
            sql.runFromClasspath("stain_protocols.sql");
        });
    }

    static class ProtocolsRepository {

        final EntityManager<StainProtocol, Long> stainProtEntityManager;
        final EntityManager<AdvancedStainProtocol, AdvancedStainProtocol.AdvancedStainProtocolId> advStainProtEntityManager;

        public ProtocolsRepository(DataAccesFacade facade) {
            this.stainProtEntityManager = new EntityManagerBuilder(facade).build(StainProtocol.class);
            this.advStainProtEntityManager = new EntityManagerBuilder(facade).build(AdvancedStainProtocol.class);
        }

        public Long createNewStain(String name) {
            var newStain = new StainProtocol();
            newStain.setId(null);
            newStain.setName(name);
            stainProtEntityManager.insert(newStain);
            return newStain.getId();
        }

        public void renameStain(Long stainId, String name) {
            var stain = stainProtEntityManager.loadById(stainId);
            stain.setName(name);
            stainProtEntityManager.update(stain);
        }

        public void deleteStain(Long stainId) {

            advStainProtEntityManager.createQuery("a")
                    .append("delete from {a} where {a.id.idStain=?}", stainId)
                    .execute()
                    .update()
            ;

            stainProtEntityManager.deleteById(stainId);
        }

        public void setAsAdvanced(Long stainId, String host) {

            var id = new AdvancedStainProtocol.AdvancedStainProtocolId();
            id.setHost(host);
            id.setIdStain(stainId);

            if (!advStainProtEntityManager.existsById(id)) {

                var adv = new AdvancedStainProtocol();
                adv.setId(id);
                advStainProtEntityManager.insert(adv);
            }
        }

        public void unsetAsAdvanced(Long stainId, String host) {
            var id = new AdvancedStainProtocol.AdvancedStainProtocolId();
            id.setHost(host);
            id.setIdStain(stainId);
            advStainProtEntityManager.deleteById(id);
        }

        public List<String> getAdvStainProtocolsForHost(String host) {
            return stainProtEntityManager.createQuery("sp")
                    .addAlias("adv", advStainProtEntityManager)
                    .append(
                            "select {sp.name} from {sp} join {adv} on {sp.id}={adv.id.idStain} where {adv.id.host=?}",
                            host)
                    .execute()
                    .loadScalars(ScalarMappers.STRING)
                    ;
        }
    }

    @Test
    void name() {

        var repo = new ProtocolsRepository(facade);
        transactionalOps.run(() -> {
            assertThat(repo.getAdvStainProtocolsForHost("LI")).hasToString("[]");

            var idStain = repo.createNewStain("123");
            repo.setAsAdvanced(idStain, "LI");

            assertThat(repo.getAdvStainProtocolsForHost("LI")).hasToString("[123]");

            repo.renameStain(idStain, "666");

            assertThat(repo.getAdvStainProtocolsForHost("LI")).hasToString("[666]");
            assertThat(repo.getAdvStainProtocolsForHost("LO")).hasToString("[]");

            repo.deleteStain(idStain);

            assertThat(repo.getAdvStainProtocolsForHost("LI")).hasToString("[]");
        });
    }
}

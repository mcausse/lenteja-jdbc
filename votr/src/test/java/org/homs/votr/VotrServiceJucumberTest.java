package org.homs.votr;

import io.homs.jucumber.JucumberJUnit5Runner;
import io.homs.jucumber.anno.*;
import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityManagerBuilder;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.JdbcDataAccesFacade;
import org.homs.lentejajdbc.TransactionalUtils;
import org.homs.lentejajdbc.extractor.MapRowMapper;
import org.homs.lentejajdbc.script.SqlScriptExecutor;
import org.homs.votr.ent.Message;
import org.homs.votr.ent.Option;
import org.homs.votr.ent.User;
import org.homs.votr.ent.Votr;
import org.homs.votr.repo.MessageRepository;
import org.homs.votr.repo.OptionRepository;
import org.homs.votr.repo.UserRepository;
import org.homs.votr.repo.VotrRepository;
import org.homs.votr.util.DateUtil;
import org.homs.votr.util.UUIDUtils;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = {MockitoExtension.class, JucumberJUnit5Runner.class})
@Feature("VotrService")
public class VotrServiceJucumberTest {

    @Mock
    DateUtil dateUtil;
    @Mock
    UUIDUtils uUIDUtils;

    VotrService votrService;
    Votr votr;

    final DataAccesFacade facade;

    public VotrServiceJucumberTest() {
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

        var entityManagerBuilder = new EntityManagerBuilder(facade);
        var votrRepo = new VotrRepository(entityManagerBuilder.build(Votr.class));
        var userRepo = new UserRepository(entityManagerBuilder.build(User.class));
        var optionsRepo = new OptionRepository(entityManagerBuilder.build(Option.class));
        var messagesRepo = new MessageRepository(entityManagerBuilder.build(Message.class));
        this.votrService = new VotrService(votrRepo, userRepo, optionsRepo, messagesRepo, uUIDUtils, dateUtil);
    }

    @Given("a Votr named as '(.+?)' and created by '(.+?)'")
    @When("a Votr is created as '(.+?)' and by '(.+?)'")
    public void createVotr(String votrName, String creationUserEmail) {
        var user = new User();
        user.setEmail(creationUserEmail);

        Mockito.when(uUIDUtils.createUUID(Mockito.any())).thenReturn("12345", "67890");
        Mockito.when(dateUtil.now()).thenReturn(new Date(0L));

        this.votr = TransactionalUtils.runWithReturn(facade, () -> votrService.createVotr(votrName, votrName + " desc", user));

//        Mockito.verify(uUIDUtils, Mockito.times(2)).createUUID(Mockito.any());
//        Mockito.verify(dateUtil, Mockito.times(2)).now();
    }

    @Then("a Votr exists as '(.+?)' and created by '(.+?)'")
    public void assertVotrExists(String votrName, String creationUserEmail) {
        assertThat(
                queryForTest(Votr.class,
                "select {o.votrHash},{o.title},{o.creationUserHash} from {o} where {o.votrHash=?}",
                votr.getVotrHash()))
                .hasToString("[{VOTR_HASH=12345, TITLE=" + votrName + ", CREAT_USER_HASH=67890}]");
    }

    @When("a new user is created as '(.+?)'")
    public void createUser(String userEmail) {
        Mockito.when(uUIDUtils.createUUID(Mockito.any())).thenReturn("ABCDEF");
        Mockito.when(dateUtil.now()).thenReturn(new Date(0L));

        TransactionalUtils.run(facade, () -> votrService.createUser(votr.getVotrHash(), "ave@votr.org"));

//        Mockito.verify(uUIDUtils, Mockito.times(1)).createUUID(Mockito.any());
//        Mockito.verify(dateUtil, Mockito.times(1)).now();
    }

    private List<Map<String, Object>> queryForTest(Class entityClass, String query, Object... args) {
        return TransactionalUtils.runAsReadOnlyWithReturn(facade, () -> {
            EntityManager<?, ?> votrEm = new EntityManagerBuilder(facade).build(entityClass);
            return votrEm.createQuery("o").append(query, args).execute().loadScalars(new MapRowMapper());
        });
    }

    @Then("the users list is '(.+?)'")
    public void assertUsersListExists(String usersList) {
        assertThat(queryForTest(
                User.class,
                "select {o.userHash},{o.email} from {o} where {o.votrId=?}",
                votr.getVotrId()))
                .hasToString(usersList);
    }


    @Test
    @Scenario("creation of an empty Votr")
    @When("a Votr is created as 'best price' and by 'mhc@votr.org'")
    @Then("a Votr exists as 'best price' and created by 'mhc@votr.org'")
    void create_new_votr() {
    }

    @Test
    @Scenario("asdition of a new user")
    @Given("a Votr named as 'best price' and created by 'mhc@votr.org'")
    @When("a new user is created as 'ave@votr.org'")
    @Then({"a Votr exists as 'best price' and created by 'mhc@votr.org'",
            "the users list is '[{USER_HASH=67890, EMAIL=mhc@votr.org}, {USER_HASH=ABCDEF, EMAIL=ave@votr.org}]'"})
    void add_new_user() {
    }
}

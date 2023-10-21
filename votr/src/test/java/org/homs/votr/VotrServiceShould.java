package org.homs.votr;

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

@ExtendWith(MockitoExtension.class)
class VotrServiceShould {

    @Mock
    DateUtil dateUtil;
    @Mock
    UUIDUtils uUIDUtils;

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

        var votrRepo = new VotrRepository(entityManagerBuilder.build(Votr.class));
        var userRepo = new UserRepository(entityManagerBuilder.build(User.class));
        var optionsRepo = new OptionRepository(entityManagerBuilder.build(Option.class));
        var messagesRepo = new MessageRepository(entityManagerBuilder.build(Message.class));

        return new VotrService(votrRepo, userRepo, optionsRepo, messagesRepo, uUIDUtils, dateUtil);
    }

    private List<Map<String, Object>> queryForTest(Class entityClass, String query, Object... args) {
        return TransactionalUtils.runAsReadOnlyWithReturn(facade, () -> {
            EntityManager<?, ?> votrEm = new EntityManagerBuilder(facade).build(entityClass);
            return votrEm
                    .createQuery("o")
                    .append(query, args)
                    .execute()
                    .loadScalars(new MapRowMapper());
        });
    }

    @Test
    void create_new_votr() {
        VotrService service = buildVotrService();

        var user = new User();
        user.setEmail("mhc@votr.org");

        Mockito.when(uUIDUtils.createUUID(Mockito.any())).thenReturn("12345", "67890");
        Mockito.when(dateUtil.now()).thenReturn(new Date(0L));

        // Act
        Votr votr = TransactionalUtils.runWithReturn(facade, () -> service.createVotr("best poem", "best poem of all times", user));

        Mockito.verify(uUIDUtils, Mockito.times(2)).createUUID(Mockito.any());
        Mockito.verify(dateUtil, Mockito.times(2)).now();

        assertThat(queryForTest(Votr.class, "select {o.votrHash},{o.title},{o.creationUserHash} from {o} where {o.votrHash=?}", votr.getVotrHash()))
                .hasToString("[{VOTR_HASH=12345, TITLE=best poem, CREAT_USER_HASH=67890}]");
    }

    @Test
    void create_a_second_user() {
        VotrService service = buildVotrService();

        var user = new User();
        user.setEmail("mhc@votr.org");

        Mockito.when(uUIDUtils.createUUID(Mockito.any())).thenReturn("12345", "67890", "ABCDEF");
        Mockito.when(dateUtil.now()).thenReturn(new Date(0L));

        Votr votr = TransactionalUtils.runWithReturn(facade, () -> service.createVotr("best poem", "best poem of all times", user));

        // Act
        TransactionalUtils.run(facade, () -> service.createUser(votr.getVotrHash(), "ave@votr.org"));

        Mockito.verify(uUIDUtils, Mockito.times(3)).createUUID(Mockito.any());
        Mockito.verify(dateUtil, Mockito.times(3)).now();

        assertThat(queryForTest(Votr.class, "select {o.votrHash},{o.title},{o.creationUserHash} from {o} where {o.votrHash=?}", votr.getVotrHash()))
                .hasToString("[{VOTR_HASH=12345, TITLE=best poem, CREAT_USER_HASH=67890}]");
        assertThat(queryForTest(User.class, "select {o.email} from {o} where {o.votrId=?}", votr.getVotrId()))
                .hasToString("[{EMAIL=mhc@votr.org}, {EMAIL=ave@votr.org}]");
    }

    @Test
    void create_2_options() {
        VotrService service = buildVotrService();

        var user = new User();
        user.setEmail("mhc@votr.org");

        Mockito.when(uUIDUtils.createUUID(Mockito.any())).thenReturn("12345", "67890", "ABCDEF");
        Mockito.when(dateUtil.now()).thenReturn(new Date(0L));

        Votr votr = TransactionalUtils.runWithReturn(facade, () -> service.createVotr("best poem", "best poem of all times", user));

        // Act
        TransactionalUtils.run(facade, () -> {
            service.createOption(votr.getVotrHash(), "the odissey", "homer");
            service.createOption(votr.getVotrHash(), "the aeneid", "virgil");
        });

        Mockito.verify(uUIDUtils, Mockito.times(2)).createUUID(Mockito.any());
        Mockito.verify(dateUtil, Mockito.times(4)).now();

        assertThat(queryForTest(Votr.class, "select {o.votrHash},{o.title},{o.creationUserHash} from {o} where {o.votrHash=?}", votr.getVotrHash()))
                .hasToString("[{VOTR_HASH=12345, TITLE=best poem, CREAT_USER_HASH=67890}]");
        assertThat(queryForTest(Option.class, "select {o.optionId.numOrder},{o.title},{o.description} from {o} where {o.optionId.votrId=?}", votr.getVotrId()))
                .hasToString("[{NORDER=0, TITLE=the odissey, DESCR=homer}, {NORDER=1, TITLE=the aeneid, DESCR=virgil}]");
    }

    @Test
    void create_full_votr() {

        VotrService service = buildVotrService();

        var user = new User();
        user.setEmail("mhc@votr.org");

        Mockito.when(uUIDUtils.createUUID(Mockito.any())).thenReturn("12345", "67890", "ABCDEF");
        Mockito.when(dateUtil.now()).thenReturn(new Date(0L));


        // Act

        Votr votr = TransactionalUtils.runWithReturn(facade, () -> service.createVotr("best poem", "best poem of all times", user));
        TransactionalUtils.run(facade, () -> {
            service.createOption(votr.getVotrHash(), "the odissey", "homer");
            service.createOption(votr.getVotrHash(), "the aeneid", "virgil");
        });
        TransactionalUtils.run(facade, () -> service.createUser(votr.getVotrHash(), "ave@votr.org"));

        Mockito.verify(uUIDUtils, Mockito.times(3)).createUUID(Mockito.any());
        Mockito.verify(dateUtil, Mockito.times(5)).now();

        assertThat(queryForTest(Votr.class, "select {o.votrHash},{o.title},{o.creationUserHash} from {o} where {o.votrHash=?}", votr.getVotrHash()))
                .hasToString("[{VOTR_HASH=12345, TITLE=best poem, CREAT_USER_HASH=67890}]");
        assertThat(queryForTest(User.class, "select {o.email} from {o} where {o.votrId=?}", votr.getVotrId()))
                .hasToString("[{EMAIL=mhc@votr.org}, {EMAIL=ave@votr.org}]");
        assertThat(queryForTest(Option.class, "select {o.optionId.numOrder},{o.title},{o.description} from {o} where {o.optionId.votrId=?}", votr.getVotrId()))
                .hasToString("[{NORDER=0, TITLE=the odissey, DESCR=homer}, {NORDER=1, TITLE=the aeneid, DESCR=virgil}]");
        assertThat(queryForTest(Message.class, "select {o.message} from {o} where {o.votrId=?} order by {o.timestamp} asc", votr.getVotrId()))
                .hasToString(
                        "[{COMMENT=votr has been created.}, " +
                                "{COMMENT=option the odissey has been created.}, " +
                                "{COMMENT=option the aeneid has been created.}, " +
                                "{COMMENT=user ABCDEF has been created.}]");
    }

    @Test
    void create_and_load() {

        VotrService service = buildVotrService();

        var user = new User();
        user.setEmail("mhc@votr.org");

        Mockito.when(uUIDUtils.createUUID(Mockito.any())).thenReturn("12345", "67890", "ABCDEF");
        Mockito.when(dateUtil.now()).thenReturn(new Date(0L));

        Votr votr = TransactionalUtils.runWithReturn(facade, () -> service.createVotr("best poem", "best poem of all times", user));
        TransactionalUtils.run(facade, () -> {
            service.createOption(votr.getVotrHash(), "the odissey", "homer");
            service.createOption(votr.getVotrHash(), "the aeneid", "virgil");
        });
        TransactionalUtils.run(facade, () -> service.createUser(votr.getVotrHash(), "ave@votr.org"));

        // Act
        VotrService.VotrDto r = TransactionalUtils.runWithReturn(facade, () -> service.loadVotr(votr.getVotrHash(), user.getUserHash()));

        assertThat(r).hasToString(
                "VotrDto{votr=Votr{votrId=100, votrHash='12345', title='best poem', description='best poem of all times', creationDate=1970-01-01 01:00:00.0, creationUserHash='67890'}, " +
                        "loginedUser=User{userId=101, userHash='67890', email='mhc@votr.org', alias='null', votrId=100, votedOptionNumOrder='null', votedOptionDate=null}, " +
                        "creationUser=User{userId=101, userHash='67890', email='mhc@votr.org', alias='null', votrId=100, votedOptionNumOrder='null', votedOptionDate=null}, " +
                        "users=[" +
                        "User{userId=101, userHash='67890', email='mhc@votr.org', alias='null', votrId=100, votedOptionNumOrder='null', votedOptionDate=null}, " +
                        "User{userId=102, userHash='ABCDEF', email='ave@votr.org', alias='null', votrId=100, votedOptionNumOrder='null', votedOptionDate=null}], " +
                        "options=[" +
                        "Option{optionId=OptionId{votrId=100, numOrder=0}, title='the odissey', description='homer'}, " +
                        "Option{optionId=OptionId{votrId=100, numOrder=1}, title='the aeneid', description='virgil'}], " +
                        "messages=[" +
                        "Message{commentId=400, timestamp=1970-01-01 01:00:00.0, message='votr has been created.', votrId=100, userId=101}, " +
                        "Message{commentId=401, timestamp=1970-01-01 01:00:00.0, message='option the odissey has been created.', votrId=100, userId=101}, " +
                        "Message{commentId=402, timestamp=1970-01-01 01:00:00.0, message='option the aeneid has been created.', votrId=100, userId=101}, " +
                        "Message{commentId=403, timestamp=1970-01-01 01:00:00.0, message='user ABCDEF has been created.', votrId=100, userId=102}]}"
        );

    }
}

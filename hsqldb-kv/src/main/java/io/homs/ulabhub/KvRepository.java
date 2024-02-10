package io.homs.ulabhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.ScalarMappers;
import org.homs.lentejajdbc.TransactionalOps;
import org.homs.lentejajdbc.query.IQueryObject;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.function.Function;

public class KvRepository<E> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<E> entityClass;
    private final Function<E, String> idProvider;
    private final String tableName;
    private final DataAccesFacade facade;
    private final TransactionalOps transactionalOps;

    public KvRepository(DataAccesFacade facade, Class<E> entityClass, Function<E, String> idProvider) {
        this.entityClass = entityClass;
        this.tableName = getEntityTableName(entityClass);
        this.facade = facade;
        this.transactionalOps = new TransactionalOps(facade);
        this.idProvider = idProvider;

        if (!tableExists()) {
            recreateTable();
        }
    }

    public static <E> String getEntityTableName(Class<E> entityClass) {
        return entityClass.getName()
                .replace('.', '_')
                .replace('$', '_')
                .toUpperCase();
    }

    protected boolean tableExists() {
        int r = transactionalOps.runAsReadOnlyWithReturn(() -> facade.loadUnique(
                QueryObject.of("SELECT count(*) FROM information_schema.tables WHERE table_name = ?", tableName),
                ScalarMappers.INTEGER));
        return r > 0;
    }

    public void recreateTable() {
        transactionalOps.run(() -> {
            facade.update(QueryObject.of("drop table " + tableName + " if exists;"));
            facade.update(QueryObject.of("create table " + tableName + " (key longvarchar primary key, value longvarchar);"));
        });
    }

    public void store(E entity) {
        final String key = idProvider.apply(entity);
        final String value = serialize(entity);

        store(key, value);
    }

    public void store(String key, String value) {
        final IQueryObject query;
        if (existKey(key)) {
            query = QueryObject.of("update " + tableName + " set value=? where key=?", key, value);
        } else {
            query = QueryObject.of("insert into " + tableName + " (key,value) values (?,?)", key, value);
        }
        facade.update(query);
    }

    public boolean exist(E entity) {
        return existKey(idProvider.apply(entity));
    }

    public boolean existKey(String key) {
        return facade.loadUnique(
                QueryObject.of("select count(*) from " + tableName + " where key=?", key),
                ScalarMappers.INTEGER) > 0;
    }

    public E loadBy(String key) {
        final String value = loadValueBy(key);
        return deserialize(value);
    }

    public String loadValueBy(String key) {
        return facade.loadUnique(QueryObject.of("select value from " + tableName + " where key=?", key),
                ScalarMappers.STRING);
    }

    protected String serialize(E entity) {
        final String value;
        try {
            value = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.valueOf(entity), e);
        }
        return value;
    }

    protected E deserialize(String value) {
        try {
            return objectMapper.readValue(value, entityClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(value, e);
        }
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public String getTableName() {
        return tableName;
    }

    public DataAccesFacade getFacade() {
        return facade;
    }
}

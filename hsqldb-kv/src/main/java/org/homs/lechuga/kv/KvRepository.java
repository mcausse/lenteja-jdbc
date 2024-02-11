package org.homs.lechuga.kv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.ScalarMappers;
import org.homs.lentejajdbc.TransactionalOps;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.List;
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
                ScalarMappers.INTEGER,
                "SELECT count(*) FROM information_schema.tables WHERE table_name = ?", tableName
        ));
        return r > 0;
    }

    public void recreateTable() {
        transactionalOps.run(() -> {
            facade.update("drop table " + tableName + " if exists;");
            facade.update("create table " + tableName + " (key longvarchar primary key, value longvarchar);");
        });
    }

    public void store(E entity) {
        final String key = idProvider.apply(entity);
        final String value = serialize(entity);

        store(key, value);
    }

    public void store(String key, String value) {
        if (existKey(key)) {
            facade.update("update " + tableName + " set value=? where key=?", key, value);
        } else {
            facade.update("insert into " + tableName + " (key,value) values (?,?)", key, value);
        }
    }

    public boolean exist(E entity) {
        return existKey(idProvider.apply(entity));
    }

    public boolean existKey(String key) {
        return facade.loadUnique(
                ScalarMappers.INTEGER,
                "select count(*) from " + tableName + " where key=?", key) > 0;
    }

    public E loadBy(String key) {
        final String value = loadValueBy(key);
        return deserialize(value);
    }

    public String loadValueBy(String key) {
        return facade.loadUnique(ScalarMappers.STRING, "select value from " + tableName + " where key=?", key);
    }

    public enum SortBy {
        NONE, ASC, DESC
    }

    public static class KeyValue<E> {

        public final String key;
        public final E value;

        public KeyValue(String key, E value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "KeyValue{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public List<KeyValue<E>> loadAll(SortBy sortBy) {
        String query = "select key,value from " + tableName;
        if (sortBy == SortBy.ASC) {
            query += " order by key asc";
        } else if (sortBy == SortBy.DESC) {
            query += " order by key desc";
        }
        return facade.load(QueryObject.of(query),
                rs -> new KeyValue<>(
                        rs.getString("key"),
                        deserialize(rs.getString("value"))));
    }

    public void remove(E entity) {
        remove(idProvider.apply(entity));
    }

    public void remove(String key) {
        int affectedRows = facade.update("delete from " + tableName + " where key=?", key);
        if (affectedRows == 0) {
            throw new RuntimeException("not found; key=" + key);
        }
        if (affectedRows > 1) {
            throw new RuntimeException("multiple affected rows; key=" + key);
        }
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

    public TransactionalOps getTransactionalOps() {
        return transactionalOps;
    }
}

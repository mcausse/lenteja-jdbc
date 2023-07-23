package org.homs.lechuga.entity;

import org.homs.lechuga.entity.generator.GenerateOn;
import org.homs.lechuga.exception.LechugaException;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.ScalarMappers;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityModel<E> {

    final Class<E> entityClass;
    final String tableName;

    final List<EntityPropertyModel> idProperties;
    final List<EntityPropertyModel> nonIdProperties;
    final List<EntityPropertyModel> allProperties;

    final Map<String, EntityPropertyModel> propertyNamesMap;
    final List<EntityPropertyModel> autogeneratedProperties;

    public EntityModel(Class<E> entityClass, String tableName, List<EntityPropertyModel> idProperties, List<EntityPropertyModel> nonIdProperties) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.idProperties = idProperties;
        this.nonIdProperties = nonIdProperties;

        this.allProperties = new ArrayList<>();
        this.allProperties.addAll(idProperties);
        this.allProperties.addAll(nonIdProperties);

        this.propertyNamesMap = allProperties.stream()
                .collect(Collectors.toMap(EntityPropertyModel::getPropertiesPath, p -> p));

        this.autogeneratedProperties = Stream.concat(idProperties.stream(), nonIdProperties.stream())
                .filter(p -> p.getGenerator() != null)
                .collect(Collectors.toList());
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public String getTableName() {
        return tableName;
    }

    public List<EntityPropertyModel> getIdProperties() {
        return idProperties;
    }

    public List<EntityPropertyModel> getNonIdProperties() {
        return nonIdProperties;
    }

    public List<EntityPropertyModel> getAllProperties() {
        return allProperties;
    }

    public Map<String, EntityPropertyModel> getPropertyNamesMap() {
        return propertyNamesMap;
    }

    public List<EntityPropertyModel> getAutogeneratedProperties() {
        return autogeneratedProperties;
    }

    public void generateBeforeInsert(DataAccesFacade facade, E entity) {
        for (var p : this.autogeneratedProperties) {
            if (p.getGenerator().getGenerateOn() == GenerateOn.BEFORE) {
                var query = p.getGenerator().getQuery();
                Object generatedValue = facade.loadUnique(query, ScalarMappers.getScalarMapperFor(p.getPropertyType()));
                p.setValue(entity, generatedValue);
            }
        }
    }

    public void generateAfterInsert(DataAccesFacade facade, E entity) {
        for (var p : this.autogeneratedProperties) {
            if (p.getGenerator().getGenerateOn() == GenerateOn.AFTER) {
                var query = p.getGenerator().getQuery();
                Object generatedValue = facade.loadUnique(query, ScalarMappers.getScalarMapperFor(p.getPropertyType()));
                p.setValue(entity, generatedValue);
            }
        }
    }

    public QueryObject queryForLoadById(Object id) {
        QueryObject q = new QueryObject();
        q.append("select ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : allProperties) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(" from ");
        q.append(tableName);
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : idProperties) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValueFromId(id);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        return q;
    }

    protected String processOrderBy(Order[] orders) {
        if (orders.length == 0) {
            return "";
        }

        var j = new StringJoiner(", ");
        for (Order o : orders) {
            if (!this.propertyNamesMap.containsKey(o.getPropName())) {
                throw new LechugaException("unknown property: " + o.getPropName() + "; valid are: " + this.propertyNamesMap.keySet());
            }
            String columnName = this.propertyNamesMap.get(o.getPropName()).getColumnName();
            j.add(columnName + o.getOrder());
        }
        return " order by " + j;
    }

    public QueryObject queryForLoadAll(Order[] orders) {
        QueryObject q = new QueryObject();
        q.append("select ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : allProperties) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(" from ");
        q.append(tableName);
        q.append(processOrderBy(orders));
        return q;
    }

    public QueryObject queryForLoadByProp(String propertyName, Object value, Order[] orders) {
        if (!propertyNamesMap.containsKey(propertyName)) {
            throw new LechugaException("unknown property: " + propertyName + " for " + this);
        }

        QueryObject q = new QueryObject();
        q.append("select ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : allProperties) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(" from ");
        q.append(tableName);
        q.append(" where ");

        EntityPropertyModel c = propertyNamesMap.get(propertyName);
        q.append(c.getColumnName());
        q.append("=?");

        q.addArg(c.convertValueForJdbc(value));
        q.append(processOrderBy(orders));
        return q;
    }

    public QueryObject queryForInsert(E entity) {
        QueryObject q = new QueryObject();
        q.append("insert into ");
        q.append(tableName);
        q.append(" (");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : allProperties) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(") values (");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : allProperties) {
                j.add("?");
                Object value = c.getValue(entity);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        q.append(")");
        return q;
    }

    public QueryObject queryForUpdate(E entity) {
        QueryObject q = new QueryObject();
        q.append("update ");
        q.append(tableName);
        q.append(" set ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : nonIdProperties) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValue(entity);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : idProperties) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValue(entity);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }

        return q;
    }

    public QueryObject queryForDeleteById(Object id) {
        QueryObject q = new QueryObject();
        q.append("delete from ");
        q.append(tableName);
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : idProperties) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValueFromId(id);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        return q;
    }

    public QueryObject queryForDelete(E entity) {
        QueryObject q = new QueryObject();
        q.append("delete from ");
        q.append(tableName);
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : idProperties) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValue(entity);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        return q;
    }

    public QueryObject queryForExists(E entity) {
        QueryObject q = new QueryObject();
        q.append("select count(*) from ");
        q.append(tableName);
        q.append(" where ");

        StringJoiner j = new StringJoiner(" and ");
        for (EntityPropertyModel id : idProperties) {
            j.add(id.getColumnName() + "=?");
            Object value = id.getValue(entity);
            q.addArg(id.convertValueForJdbc(value));
        }
        q.append(j.toString());
        return q;
    }

    public QueryObject queryForExistsById(Object id) {
        QueryObject q = new QueryObject();
        q.append("select count(*) from ");
        q.append(tableName);
        q.append(" where ");

        StringJoiner j = new StringJoiner(" and ");
        for (EntityPropertyModel c : idProperties) {
            j.add(c.getColumnName() + "=?");
            Object value = c.getValueFromId(id);
            q.addArg(c.convertValueForJdbc(value));
        }
        q.append(j.toString());
        return q;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "entityClass=" + entityClass +
                ", tableName='" + tableName + '\'' +
                ", properties=" + allProperties +
                '}';
    }
}
package org.homs.lechuga.entity;

import org.homs.lechuga.exception.LechugaException;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.StringJoiner;

public class EntityManagerSqlOperations {

    final EntityModel<?> entityModel;

    public EntityManagerSqlOperations(EntityModel<?> entityModel) {
        this.entityModel = entityModel;
    }

    public QueryObject queryForLoadById(Object id) {
        QueryObject q = new QueryObject();
        q.append("select ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : entityModel.getAllProperties()) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(" from ");
        q.append(entityModel.getTableName());
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : entityModel.getIdProperties()) {
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
            if (!entityModel.getPropertyNamesMap().containsKey(o.getPropName())) {
                throw new LechugaException("unknown property: " + o.getPropName() + "; valid are: " + entityModel.getPropertyNamesMap().keySet());
            }
            String columnName = entityModel.getPropertyNamesMap().get(o.getPropName()).getColumnName();
            j.add(columnName + o.getOrder());
        }
        return " order by " + j;
    }

    public QueryObject queryForLoadAll(Order[] orders) {
        QueryObject q = new QueryObject();
        q.append("select ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : entityModel.getAllProperties()) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(" from ");
        q.append(entityModel.getTableName());
        q.append(processOrderBy(orders));
        return q;
    }

    public QueryObject queryForLoadByProp(String propertyName, Object value, Order[] orders) {
        if (!entityModel.getPropertyNamesMap().containsKey(propertyName)) {
            throw new LechugaException("unknown property: " + propertyName + " for " + this);
        }

        QueryObject q = new QueryObject();
        q.append("select ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : entityModel.getAllProperties()) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(" from ");
        q.append(entityModel.getTableName());
        q.append(" where ");

        EntityPropertyModel c = entityModel.getPropertyNamesMap().get(propertyName);
        q.append(c.getColumnName());
        q.append("=?");

        q.addArg(c.convertValueForJdbc(value));
        q.append(processOrderBy(orders));
        return q;
    }

    public QueryObject queryForInsert(Object entity) {
        QueryObject q = new QueryObject();
        q.append("insert into ");
        q.append(entityModel.getTableName());
        q.append(" (");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : entityModel.getAllProperties()) {
                j.add(c.getColumnName());
            }
            q.append(j.toString());
        }
        q.append(") values (");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : entityModel.getAllProperties()) {
                j.add("?");
                Object value = c.getValue(entity);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        q.append(")");
        return q;
    }

    public QueryObject queryForUpdate(Object entity) {
        QueryObject q = new QueryObject();
        q.append("update ");
        q.append(entityModel.getTableName());
        q.append(" set ");
        {
            StringJoiner j = new StringJoiner(",");
            for (EntityPropertyModel c : entityModel.getNonIdProperties()) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValue(entity);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : entityModel.getIdProperties()) {
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
        q.append(entityModel.getTableName());
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : entityModel.getIdProperties()) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValueFromId(id);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        return q;
    }

    public QueryObject queryForDelete(Object entity) {
        QueryObject q = new QueryObject();
        q.append("delete from ");
        q.append(entityModel.getTableName());
        q.append(" where ");
        {
            StringJoiner j = new StringJoiner(" and ");
            for (EntityPropertyModel c : entityModel.getIdProperties()) {
                j.add(c.getColumnName() + "=?");
                Object value = c.getValue(entity);
                q.addArg(c.convertValueForJdbc(value));
            }
            q.append(j.toString());
        }
        return q;
    }

    public QueryObject queryForExists(Object entity) {
        QueryObject q = new QueryObject();
        q.append("select count(*) from ");
        q.append(entityModel.getTableName());
        q.append(" where ");

        StringJoiner j = new StringJoiner(" and ");
        for (EntityPropertyModel id : entityModel.getIdProperties()) {
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
        q.append(entityModel.getTableName());
        q.append(" where ");

        StringJoiner j = new StringJoiner(" and ");
        for (EntityPropertyModel c : entityModel.getIdProperties()) {
            j.add(c.getColumnName() + "=?");
            Object value = c.getValueFromId(id);
            q.addArg(c.convertValueForJdbc(value));
        }
        q.append(j.toString());
        return q;
    }

}

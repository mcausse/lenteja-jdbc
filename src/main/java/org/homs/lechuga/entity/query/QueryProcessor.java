package org.homs.lechuga.entity.query;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityPropertyModel;
import org.homs.lentejajdbc.query.IQueryObject;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor {

    final Map<String, EntityManager<?, ?>> aliases = new LinkedHashMap<>();
    final QueryObject queryObject = new QueryObject();

    public QueryProcessor addAlias(String alias, EntityManager<?, ?> entityManager) {
        this.aliases.put(alias, entityManager);
        return this;
    }

    public QueryProcessor append(String queryFragment, Object... args) {
        IQueryObject processedQuery = processQuery(queryFragment, List.of(args).iterator());
        this.queryObject.append(processedQuery);
        return this;
    }

    protected IQueryObject processQuery(String queryFragment, Iterator<Object> args) {
        QueryObject qo = new QueryObject();
        Pattern p = Pattern.compile("\\{(\\w+)(\\.([a-zA-Z0-9.*#:]+)([^}]*))?\\}");
        Matcher m = p.matcher(queryFragment);

        int pos = 0;
        while (m.find()) {
            qo.append(queryFragment.substring(pos, m.start()));
            qo.append(processExpression(m.group(1), m.group(3), m.group(4), args));
            pos = m.end();
        }
        qo.append(queryFragment.substring(pos));

        if (args.hasNext()) {
            throw new RuntimeException("not all of the arguments has been consumed for the query fragment: " + qo);
        }

        return qo;
    }

    protected IQueryObject processExpression(String alias, String propertyExpression, String restOfExpression, Iterator<Object> args) {
        if (!this.aliases.containsKey(alias)) {
            throw new RuntimeException("alias not defined: '" + alias + "'; defined are: " + this.aliases.keySet());
        }
        EntityManager<?, ?> em = aliases.get(alias);

        QueryObject r = new QueryObject();
        if ("*".equals(propertyExpression)) {
            StringJoiner j = new StringJoiner(", ");
            for (var p : em.getEntityModel().getAllProperties()) {
                j.add(p.getColumnName());
            }
            r.append(j.toString());
        } else if (propertyExpression == null || propertyExpression.isEmpty() || propertyExpression.equals("#")) {
            r.append(em.getEntityModel().getTableName());
            r.append(" ");
            r.append(alias);
        } else {

            boolean hiddenPropertyName = false;
            if (propertyExpression.startsWith(":")) {
                propertyExpression = propertyExpression.substring(1);
                hiddenPropertyName = true;
            }
            if (!em.getEntityModel().getPropertyNamesMap().containsKey(propertyExpression)) {
                throw new RuntimeException("property not defined: '" + propertyExpression +
                        "' in entity: " + em.getEntityModel().toString() +
                        "; defined are: " + em.getEntityModel().getPropertyNamesMap());
            }
            EntityPropertyModel p = em.getEntityModel().getPropertyNamesMap().get(propertyExpression);

            if (!hiddenPropertyName) {
                r.append(alias);
                r.append(".");
                r.append(p.getColumnName());
            }
            r.append(restOfExpression);

            int pos = restOfExpression.indexOf("?");
            while (pos >= 0) {
                Object nextArgument = args.next();
                Object convertedNextArgument = p.convertValueForJdbc(nextArgument);
                r.addArg(convertedNextArgument);

                pos = restOfExpression.indexOf("?", pos + 1);
            }
        }
        return r;
    }

    public QueryObject getQueryObject() {
        return queryObject;
    }
}

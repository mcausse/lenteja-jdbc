package org.homs.lechuga.entity.query;

import org.homs.lechuga.entity.EntityManager;
import org.homs.lechuga.entity.EntityPropertyModel;
import org.homs.lechuga.exception.LechugaException;
import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.Mapable;
import org.homs.lentejajdbc.query.IQueryObject;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor<E> {

    protected static final Pattern QUERY_EXPR_PATTERN = Pattern.compile("\\{" +
            "(\\w+)" +  // alias
            "(\\." +
            /**/"([\\w.*]+)" + // property path
            /**/"([^}]*)" + // rest of expression
            ")?" +
            "\\}"
    );

    final DataAccesFacade facade;
    final Mapable<E> rowMapper;

    final Map<String, EntityManager<?, ?>> aliases;
    final QueryObject queryObject;

    public QueryProcessor(DataAccesFacade facade, Mapable<E> rowMapper) {
        this.facade = facade;
        this.rowMapper = rowMapper;
        this.aliases = new LinkedHashMap<>();
        this.queryObject = new QueryObject();
    }

    public QueryProcessor<E> addAlias(String alias, EntityManager<?, ?> entityManager) {
        this.aliases.put(alias, entityManager);
        return this;
    }

    public QueryProcessor<E> append(String queryFragment, Object... args) {
        IQueryObject processedQuery = processQuery(queryFragment, List.of(args).iterator());
        this.queryObject.append(processedQuery);
        return this;
    }

    public QueryProcessorExecutor<E> execute() {
        return new QueryProcessorExecutor<>(facade, queryObject, rowMapper);
    }

    protected IQueryObject processQuery(String queryFragment, Iterator<Object> args) {
        QueryObject qo = new QueryObject();
        Matcher m = QUERY_EXPR_PATTERN.matcher(queryFragment);

        try {

            int pos = 0;
            while (m.find()) {
                qo.append(queryFragment.substring(pos, m.start()));
                String alias = m.group(1);
                String propertyPath = m.group(3);
                String restOfExpression = m.group(4);
                qo.append(processExpression(alias, propertyPath, restOfExpression, args));
                pos = m.end();
            }
            qo.append(queryFragment.substring(pos));

            if (args.hasNext()) {
                throw new LechugaException("not all of the arguments has been consumed for the query fragment");
            }

        } catch (Exception e) {
            throw new LechugaException("error in the query fragment: " + queryFragment, e);
        }

        return qo;
    }

    protected IQueryObject processExpression(String alias, String propertyExpression, String restOfExpression, Iterator<Object> args) {
        if (!this.aliases.containsKey(alias)) {
            throw new LechugaException("alias not defined: '" + alias + "'; defined are: " + this.aliases.keySet());
        }
        EntityManager<?, ?> em = aliases.get(alias);

        QueryObject r = new QueryObject();

        /*
         * {p} => tablename p
         */
        if (propertyExpression == null || propertyExpression.isEmpty()) {
            r.append(em.getEntityModel().getTableName());
            r.append(" ");
            r.append(alias);
            return r;
        }

        /*
         * {p.*} => all mapped fields
         */
        if ("*".equals(propertyExpression)) {
            StringJoiner j = new StringJoiner(", ");
            for (var p : em.getEntityModel().getAllProperties()) {
                j.add(p.getColumnName());
            }
            r.append(j.toString());
            return r;
        }

        /*
         * {p.propertyPath...}
         */
        if (!em.getEntityModel().getPropertyNamesMap().containsKey(propertyExpression)) {
            throw new LechugaException("property not defined: '" + propertyExpression +
                    "' in entity: " + em.getEntityModel().toString() +
                    "; defined are: " + em.getEntityModel().getPropertyNamesMap());
        }
        EntityPropertyModel p = em.getEntityModel().getPropertyNamesMap().get(propertyExpression);

        /*
         * {p.propertyPath?}
         */
        if (!"?".equals(restOfExpression)) {
            r.append(alias);
            r.append(".");
            r.append(p.getColumnName());
        }
        r.append(restOfExpression);

        int pos = restOfExpression.indexOf("?");
        while (pos >= 0) {
            if (!args.hasNext()) {
                throw new LechugaException("all of the arguments has been consumed for the query fragment");
            }
            Object nextArgument = args.next();
            Object convertedNextArgument = p.convertValueForJdbc(nextArgument);
            r.addArg(convertedNextArgument);

            pos = restOfExpression.indexOf("?", pos + 1);
        }
        return r;
    }

    public QueryObject getQueryObject() {
        return queryObject;
    }
}

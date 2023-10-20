package org.homs.lentejajdbc.query;

import java.util.StringJoiner;

public class QueryObjectUtils {

    public static final int QUERY_STRING_LIMIT = 2000;

    public static IQueryObject queryFor(String fragment, Object... args) {
        QueryObject q = new QueryObject(fragment);
        for (Object arg : args) {
            q.addArg(arg);
        }
        return q;
    }

//    public static QueryObject in(Object... values) {
//        var q = new QueryObject();
//        q.append("(");
//
//        var j = new StringJoiner(",");
//        for (var value : values) {
//            j.add("?");
//            q.addArg(value);
//        }
//        q.append(j.toString());
//        q.append(")");
//        return q;
//    }

    public static String toString(IQueryObject q) {
        return toString(q.getQuery(), q.getArgs());
    }

    public static String toString(String query, Object[] args) {

        final StringBuilder r = new StringBuilder();

        int index = Math.min(QUERY_STRING_LIMIT, query.length());
        query = query.substring(0, index);

        r.append(query);
        r.append(" -- [");
        int c = 0;
        for (final Object o : args) {
            if (c > 0) {
                r.append(", ");
            }
            r.append(o);
            if (o != null) {
                r.append("(");
                r.append(o.getClass().getSimpleName());
                r.append(")");
            }
            c++;
        }
        r.append("]");
        return r.toString();
    }

}

package org.homs.lechuga.entity.generator.impl;

import org.homs.lechuga.entity.generator.Generator;
import org.homs.lentejajdbc.query.IQueryObject;

public class AbstractGenerator implements Generator {

    final IQueryObject query;
    final boolean beforeInsert;

    public AbstractGenerator(IQueryObject query, boolean beforeInsert) {
        super();
        this.query = query;
        this.beforeInsert = beforeInsert;
    }

    @Override
    public IQueryObject getQuery() {
        return query;
    }

    @Override
    public boolean isBeforeInsert() {
        return beforeInsert;
    }

}
package org.homs.lechuga.entity.generator;

import org.homs.lentejajdbc.query.IQueryObject;

public interface Generator {

    boolean isBeforeInsert();

    IQueryObject getQuery();
}
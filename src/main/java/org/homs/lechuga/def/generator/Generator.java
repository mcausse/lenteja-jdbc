package org.homs.lechuga.def.generator;

import org.homs.lentejajdbc.query.IQueryObject;

public interface Generator {

    boolean isBeforeInsert();

    IQueryObject getQuery();
}
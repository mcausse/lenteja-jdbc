package org.homs.lechuga.entity.generator;


import org.homs.lentejajdbc.query.IQueryObject;

public interface Generator {

    GenerateOn getGenerateOn();

    IQueryObject getQuery();
}
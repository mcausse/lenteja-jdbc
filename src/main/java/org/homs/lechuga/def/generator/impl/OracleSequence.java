package org.homs.lechuga.def.generator.impl;

import org.homs.lentejajdbc.query.QueryObject;

public class OracleSequence extends AbstractGenerator {

    public OracleSequence(String sequenceName) {
        super(new QueryObject("select " + sequenceName + ".nextval from dual"), true);
    }
}
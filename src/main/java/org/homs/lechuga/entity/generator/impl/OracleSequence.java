package org.homs.lechuga.entity.generator.impl;

import org.homs.lechuga.entity.generator.GenerateOn;
import org.homs.lentejajdbc.query.QueryObject;

public class OracleSequence extends AbstractGenerator {

    public OracleSequence(String sequenceName) {
        super(new QueryObject("select " + sequenceName + ".nextval from dual"), GenerateOn.BEFORE);
    }
}
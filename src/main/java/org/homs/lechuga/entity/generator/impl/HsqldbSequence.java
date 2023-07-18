package org.homs.lechuga.entity.generator.impl;

import org.homs.lentejajdbc.query.QueryObject;

public class HsqldbSequence extends AbstractGenerator {

    public HsqldbSequence(String sequenceName) {
        super(new QueryObject("call next value for " + sequenceName), true);
    }
}
package org.homs.lechuga.entity.generator.impl;

import org.homs.lentejajdbc.query.QueryObject;

public class HsqldbIdentity extends AbstractGenerator {

    public HsqldbIdentity() {
        super(new QueryObject("call identity()"), false);
    }
}
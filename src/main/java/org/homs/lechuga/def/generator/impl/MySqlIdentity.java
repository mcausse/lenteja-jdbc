package org.homs.lechuga.def.generator.impl;

import org.homs.lentejajdbc.query.QueryObject;

public class MySqlIdentity extends AbstractGenerator {

    public MySqlIdentity() {
        super(new QueryObject("select last_insert_id()"), false);
    }
}
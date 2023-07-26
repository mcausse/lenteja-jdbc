package org.homs.lechuga.entity.generator.impl;

import org.homs.lechuga.entity.generator.GenerateOn;
import org.homs.lechuga.entity.generator.Generator;
import org.homs.lentejajdbc.query.IQueryObject;

public class AbstractGenerator implements Generator {

    final IQueryObject query;
    final GenerateOn generateOn;

    public AbstractGenerator(IQueryObject query, GenerateOn generateOn) {
        super();
        this.query = query;
        this.generateOn = generateOn;
    }

    @Override
    public IQueryObject getQuery() {
        return query;
    }

    @Override
    public GenerateOn getGenerateOn() {
        return generateOn;
    }

}
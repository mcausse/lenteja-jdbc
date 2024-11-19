package org.homs.rules;

import lombok.Data;
import org.homs.lechuga.entity.anno.Enumerated;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbIdentity;

@Data
@Table
public class RuleCondition {

    public enum ERuleOperator {
        EXACT_MATCH, STARTS_WITH;
    }

    @Id
    @Generated(HsqldbIdentity.class)
    Long id;

    String entity;
    String field;

    @Enumerated
    ERuleOperator operator;

    Long idRule;
}

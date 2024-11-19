package org.homs.rules;

import lombok.Data;
import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbIdentity;

@Data
@Table("rule_set")
public class RuleSet {

    @Id
    @Generated(HsqldbIdentity.class)
    Long id;

    @Column("description")
    String desc;
}

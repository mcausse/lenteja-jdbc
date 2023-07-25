package org.homs.lechuga.votr;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

@Table("users")
public class User {

    @Id
    @Generated(value = HsqldbSequence.class, args = {"seq_votrs"})
    Integer userId;

    String userHash;

    String email;
    String alias;

    Integer votrId;

    String votedOptionNumOrder;
    String votedOptionDate;

}

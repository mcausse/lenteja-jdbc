package org.homs.votr.infra.jdbc.entity;

import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

@Table("users")
public class LechugaUser {

    @Id
    @Generated(value = HsqldbSequence.class, args = {"seq_votrs"})
    Integer userId;

    String userHash;

    String email;
    String alias;

    Integer votrId;

    String votedOptionNumOrder;
    String votedOptionDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getVotrId() {
        return votrId;
    }

    public void setVotrId(Integer votrId) {
        this.votrId = votrId;
    }

    public String getVotedOptionNumOrder() {
        return votedOptionNumOrder;
    }

    public void setVotedOptionNumOrder(String votedOptionNumOrder) {
        this.votedOptionNumOrder = votedOptionNumOrder;
    }

    public String getVotedOptionDate() {
        return votedOptionDate;
    }

    public void setVotedOptionDate(String votedOptionDate) {
        this.votedOptionDate = votedOptionDate;
    }
}

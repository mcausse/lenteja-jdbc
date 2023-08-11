package org.homs.votr.ent;

import org.homs.lechuga.entity.anno.Column;

public class OptionId {

    Integer votrId;

    @Column("norder")
    Integer numOrder;

    public Integer getVotrId() {
        return votrId;
    }

    public void setVotrId(Integer votrId) {
        this.votrId = votrId;
    }

    public Integer getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(Integer numOrder) {
        this.numOrder = numOrder;
    }

    @Override
    public String toString() {
        return "OptionId{" +
                "votrId=" + votrId +
                ", numOrder=" + numOrder +
                '}';
    }
}

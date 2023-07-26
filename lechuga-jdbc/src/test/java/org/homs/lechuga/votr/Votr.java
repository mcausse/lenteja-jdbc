package org.homs.lechuga.votr;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

import java.util.Date;

@Table("votrs")
public class Votr {

    @Id
    @Generated(value = HsqldbSequence.class, args = {"seq_votrs"})
    Integer votrId;

    String votrHash;

    String title;

    @Column("descr")
    String description;

    @Column("creat_date")
    Date creationDate;

    public void setVotrId(Integer votrId) {
        this.votrId = votrId;
    }

    public void setVotrHash(String votrHash) {
        this.votrHash = votrHash;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Votr{" +
                "votrId=" + votrId +
                ", votrHash='" + votrHash + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}

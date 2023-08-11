package trash.infra.jdbc.entity;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

import java.util.Date;

@Table("votrs")
public class LechugaVotr {

    @Id
    @Generated(value = HsqldbSequence.class, args = {"seq_votrs"})
    Integer votrId;

    String votrHash;

    String title;

    @Column("descr")
    String description;

    @Column("creat_date")
    Date creationDate;
    @Column("creat_user_hash")
    String creationUserHash;

    public Integer getVotrId() {
        return votrId;
    }

    public void setVotrId(Integer votrId) {
        this.votrId = votrId;
    }

    public String getVotrHash() {
        return votrHash;
    }

    public void setVotrHash(String votrHash) {
        this.votrHash = votrHash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationUserHash() {
        return creationUserHash;
    }

    public void setCreationUserHash(String creationUserHash) {
        this.creationUserHash = creationUserHash;
    }

    @Override
    public String toString() {
        return "LechugaVotr{" +
                "votrId=" + votrId +
                ", votrHash='" + votrHash + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", creationUserHash='" + creationUserHash + '\'' +
                '}';
    }
}

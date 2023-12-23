package io.homs.votr.votr.infra;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

@Table("votrs")
public class LechugaOption {

    @Id
    String uuid;

    String name;
    @Column("descr")
    String description;

    String votrUuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVotrUuid() {
        return votrUuid;
    }

    public void setVotrUuid(String votrUuid) {
        this.votrUuid = votrUuid;
    }
}

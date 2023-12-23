package io.homs.votr.votr.infra;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;

@Table("votrs")
public class LechugaVotr {

    @Id
    String uuid;

    String name;
    @Column("descr")
    String description;

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
}

package io.homs.votr.votr.infra;

import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;

@Table("votrs")
public class LechugaUser {

    @Id
    String uuid;

    String email;
    String alias;

    String votrUuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getVotrUuid() {
        return votrUuid;
    }

    public void setVotrUuid(String votrUuid) {
        this.votrUuid = votrUuid;
    }
}

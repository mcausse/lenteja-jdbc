package io.homs.votr.messages.infra;

import org.homs.lechuga.entity.anno.Handler;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.handlers.impl.DateStringHandler;

import java.util.Date;

@Table("messages")
public class LechugaMessage {

    @Id
    private String uuid;

    private String votrUuid;
    private String userUuid;

    @Handler(value = DateStringHandler.class, args = "yyyyMMdd HH:mm:ss.SSS")
    private Date posted;

    private String message;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVotrUuid() {
        return votrUuid;
    }

    public void setVotrUuid(String votrUuid) {
        this.votrUuid = votrUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
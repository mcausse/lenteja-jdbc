package org.homs.votr.ent;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Generated;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;
import org.homs.lechuga.entity.generator.impl.HsqldbSequence;

import java.util.Date;

@Table("comments")
public class Message {

    @Id
    @Column("comment_id")
    @Generated(value = HsqldbSequence.class, args = {"seq_comments"})
    Long commentId;

    @Column("comment_date")
    Date timestamp;

    @Column("comment")
    String message;

    int votrId;
    long userId;

    public Message() {
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getVotrId() {
        return votrId;
    }

    public void setVotrId(int votrId) {
        this.votrId = votrId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "commentId=" + commentId +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", votrId=" + votrId +
                ", userId=" + userId +
                '}';
    }
}

package trash.infra.jdbc.entity;

import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;

import java.util.Date;

@Table("comments")
public class LechugaMessage {

    @Id
    @Column("comment_id")
    Long commentId;

    @Column("comment_date")
    Date timestamp;

    @Column("comment")
    String message;

    int votrId;
    long userId;

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
}

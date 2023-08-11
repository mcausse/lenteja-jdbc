package org.homs.votr.ent;


import org.homs.lechuga.entity.anno.Column;
import org.homs.lechuga.entity.anno.Embedded;
import org.homs.lechuga.entity.anno.Id;
import org.homs.lechuga.entity.anno.Table;

@Table("options")
public class Option {

    @Id
    @Embedded
    OptionId optionId;

    String title;
    @Column("descr")
    String description;

    public OptionId getOptionId() {
        return optionId;
    }

    public void setOptionId(OptionId optionId) {
        this.optionId = optionId;
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

    @Override
    public String toString() {
        return "Option{" +
                "optionId=" + optionId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

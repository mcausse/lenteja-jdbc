package org.homs.lechuga;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.homs.lechuga.entity.anno.Embedded;
import org.homs.lechuga.entity.anno.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colr405 {

    @Embedded
    @Id
    Colr405Id id;

    String value;

    @Override
    public String toString() {
        return "Colr405{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}

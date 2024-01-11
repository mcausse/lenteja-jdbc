package org.homs.lechuga;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colr405Id {

    private String guid;
    private Integer version;

    @Override
    public String toString() {
        return "Colr405Id{" +
                "guid='" + guid + '\'' +
                ", version=" + version +
                '}';
    }
}
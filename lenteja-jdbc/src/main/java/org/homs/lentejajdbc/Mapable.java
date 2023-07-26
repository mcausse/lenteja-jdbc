package org.homs.lentejajdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Aquesta interfície tributa la capacitat d'extreure un valor tipat d'un
 * {@link ResultSet}. Aquest valor pot ser una entitat, o un valor escalar.
 *
 * @param <T> resultat del mapping
 * @author mhoms
 */
public interface Mapable<T> {

    T map(ResultSet rs) throws SQLException;

}
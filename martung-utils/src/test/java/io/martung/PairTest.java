package io.martung;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class PairTest {

    @Test
    void build() {
        var m = new LinkedHashMap<Integer, String>();

        Pair.of(1, "one").putToMap(m);
        Pair.of(2, "two").putToMap(m);
        Pair.of(3, "thr").putToMap(m);

        assertThat(m).hasToString("{1=one, 2=two, 3=thr}");
    }

    @Test
    void build2() {
        var p = Pair.of(1, "one");

        assertThat(p).isSameAs(p);
        assertThat(p).isEqualTo(p);
        assertThat(p).isEqualTo(Pair.of(1, "one"));
        assertThat(p).isNotEqualTo(Pair.of(2, "one"));
        assertThat(p).isNotEqualTo(Pair.of(1, "two"));
        assertThat(p).isNotEqualTo(1234L);
        assertThat(p).hasToString("Pair{left=1, right=one}");
    }
}
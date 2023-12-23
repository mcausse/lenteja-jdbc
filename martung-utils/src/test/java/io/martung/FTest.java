package io.martung;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class FTest {

    @Test
    void forEach() {

        Function<Integer, Integer> mulBy2 = e -> e * 2;
        Function<Integer, Integer> mulBy3 = e -> e * 3;
        List<Integer> r = new ArrayList<>();

        F.forEach(
                List.of(1, 2, 3, 4, 5),
                e -> r.add(mulBy2.andThen(mulBy3).apply(e)));

        assertThat(r).hasToString("[6, 12, 18, 24, 30]");
    }

    @Test
    void map() {

        Function<Integer, Integer> mulBy2 = e -> e * 2;
        Function<Integer, Integer> mulBy3 = e -> e * 3;

        List<Integer> r = F.map(
                List.of(1, 2, 3, 4, 5),
                mulBy2.andThen(mulBy3));

        assertThat(r).hasToString("[6, 12, 18, 24, 30]");
    }

    @Test
    void filter() {

        Predicate<Integer> isOdd = e -> e % 2 != 0;
        Predicate<Integer> is2 = e -> e == 2;

        List<Integer> r = F.filter(
                List.of(1, 2, 3, 4, 5),
                isOdd.or(is2));

        assertThat(r).hasToString("[1, 2, 3, 5]");
    }

    @Test
    void neg_filter() {

        Predicate<Integer> isOdd = e -> e % 2 != 0;
        Predicate<Integer> is2 = e -> e == 2;

        List<Integer> r = F.filter(
                List.of(1, 2, 3, 4, 5),
                isOdd.or(is2).negate());

        assertThat(r).hasToString("[4]");
    }

    @Test
    void basic_reduce_with_initial_value() {

        Integer r = F.reduce(
                List.of(1, 2, 3, 4, 5),
                5,
                Integer::sum
        );

        assertThat(r).isEqualTo(20);
    }

    @Test
    void reduce_operation_with_empty_list_should_return_the_initial_value() {

        Integer r = F.reduce(
                List.of(),
                5,
                Integer::sum
        );

        assertThat(r).isEqualTo(5);
    }


    @Test
    void reduce_with_no_initial_value_should_give_the_difference() {

        String r1 = F.reduce(List.of("1", "2", "3"),
                "",
                (a, b) -> a + "," + b);
        assertThat(r1).hasToString(",1,2,3");

        String r2 = F.reduce(List.of("1", "2", "3"),
                (a, b) -> a + "," + b);
        assertThat(r2).hasToString("1,2,3");
    }

    @Test
    void mapMapEntries() {
        Map<Integer, String> m = F.enmap(
                F.enpair(1, "one"),
                F.enpair(2, "two"),
                F.enpair(3, "three")
        );

        Map<String, String> r = F.map(m,
                (k, v) -> String.valueOf(k * 2),
                (k, v) -> v);

        assertThat(r).hasToString("{2=one, 4=two, 6=three}");
    }

    @Test
    void filterMapEntries() {
        Map<Integer, String> m = F.enmap(
                F.enpair(1, "one"),
                F.enpair(2, "two"),
                F.enpair(3, "three")
        );

        Map<Integer, String> r = F.filter(m, (k, v) -> k.equals(3) || v.equals("two"));

        assertThat(r).hasToString("{2=two, 3=three}");
    }

    @Test
    void projectMapEntries() {
        Map<Integer, String> m = F.enmap(List.of(
                F.enpair(1, "one"),
                F.enpair(2, "two"),
                F.enpair(3, "three")
        ));

        List<String> r = F.projectMapEntries(m, (k, v) -> k + ":" + v);

        assertThat(r).hasToString("[1:one, 2:two, 3:three]");
    }

    @Test
    void everyAnyList() {


        assertThat(F.any(List.of(1, 2, 3, 4, 5), o -> o.getClass().equals(Integer.class))).isTrue();
        assertThat(F.every(List.of(1, 2, 3, 4, 5), o -> o.getClass().equals(Integer.class))).isTrue();

        assertThat(F.any(List.of(1L, 2, 3, 4, 5), o -> o.getClass().equals(Integer.class))).isTrue();
        assertThat(F.every(List.of(1L, 2, 3, 4, 5), o -> o.getClass().equals(Integer.class))).isFalse();
    }

    @Test
    void everyAnyMap() {
        Map<Integer, String> m = F.enmap(List.of(
                F.enpair(1, "one"),
                F.enpair(2, "two"),
                F.enpair(3, "three")
        ));

        assertThat(F.any(m, (k, v) -> true)).isTrue();
        assertThat(F.every(m, (k, v) -> true)).isTrue();

        assertThat(F.any(m, (k, v) -> v.equals("two"))).isTrue();
        assertThat(F.every(m, (k, v) -> v.equals("two"))).isFalse();
    }
}
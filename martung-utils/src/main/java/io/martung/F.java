package io.martung;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

public final class F {

    private F() {
    }

    public static <K, V> Pair<K, V> enpair(K k, V v) {
        return new Pair<>(k, v);
    }

    public static <K, V> Map<K, V> enmap(Pair<K, V>... pairs) {
        return enmap(List.of(pairs));
    }

    public static <K, V> Map<K, V> enmap(Iterable<Pair<K, V>> pairs) {
        Map<K, V> r = new LinkedHashMap<>();
        for (Pair<K, V> pair : pairs) {
            r.put(pair.left, pair.right);
        }
        return r;
    }

    public static <T> void forEach(Iterable<T> iterable, Consumer<T> op) {
        for (var elem : iterable) {
            op.accept(elem);
        }
    }

    public static <S, D> List<D> map(Iterable<S> iterable, Function<S, D> function) {
        List<D> r = new ArrayList<>();
        forEach(iterable, e -> r.add(function.apply(e)));
        return r;
    }

    public static <K1, V1, K2, V2> Map<K2, V2> map(Map<K1, V1> map, BiFunction<K1, V1, K2> keysFunction, BiFunction<K1, V1, V2> valuesFunction) {
        Map<K2, V2> r = new LinkedHashMap<>();
        for (Map.Entry<K1, V1> entry : map.entrySet()) {
            r.put(
                    keysFunction.apply(entry.getKey(), entry.getValue()),
                    valuesFunction.apply(entry.getKey(), entry.getValue())
            );
        }
        return r;
    }

    public static <T> List<T> filter(Iterable<T> iterable, Predicate<T> predicate) {
        List<T> r = new ArrayList<>();
        forEach(iterable, e -> {
            if (predicate.test(e)) {
                r.add(e);
            }
        });
        return r;
    }

    public static <K, V> Map<K, V> filter(Map<K, V> map, BiPredicate<K, V> entryPredicate) {
        Map<K, V> r = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entryPredicate.test(entry.getKey(), entry.getValue())) {
                r.put(entry.getKey(), entry.getValue());
            }
        }
        return r;
    }

    public static <K, V, R> List<R> projectMapEntries(Map<K, V> map, BiFunction<K, V, R> projectFunction) {
        List<R> r = new ArrayList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            r.add(projectFunction.apply(entry.getKey(), entry.getValue()));
        }
        return r;
    }

    public static <T> T reduce(Iterable<T> iterable, T initialValue, BiFunction<T, T, T> bifunction) {
        T acumulator = initialValue;
        for (T e : iterable) {
            acumulator = bifunction.apply(acumulator, e);
        }
        return acumulator;
    }

    public static <T> T reduce(Iterable<T> iterable, BiFunction<T, T, T> bifunction) {
        T acumulator = null;
        int i = 0;
        for (T e : iterable) {
            if (i == 0) {
                acumulator = e;
            } else {
                acumulator = bifunction.apply(acumulator, e);
            }
            i++;
        }
        return acumulator;
    }

    public static <T> boolean any(Iterable<T> iterable, Predicate<T> predicate) {
        for (T e : iterable) {
            if (predicate.test(e)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean every(Iterable<T> iterable, Predicate<T> predicate) {
        for (T e : iterable) {
            if (!predicate.test(e)) {
                return false;
            }
        }
        return true;
    }

    public static <K, V> boolean any(Map<K, V> map, BiPredicate<K, V> predicate) {
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (predicate.test(e.getKey(), e.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static <K, V> boolean every(Map<K, V> map, BiPredicate<K, V> predicate) {
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (!predicate.test(e.getKey(), e.getValue())) {
                return false;
            }
        }
        return true;
    }
}

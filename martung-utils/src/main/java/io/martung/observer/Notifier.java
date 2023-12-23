package io.martung.observer;

import java.util.ArrayList;
import java.util.List;

public class Notifier<E, L extends Listener<E>> {

    private final List<L> listeners = new ArrayList<>();

    public void addListener(L listener) {
        this.listeners.add(listener);
    }

    public void notify(E event) {
        listeners.forEach(l -> l.handle(event));
    }
}
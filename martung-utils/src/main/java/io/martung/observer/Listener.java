package io.martung.observer;

public interface Listener<E> {
    void handle(E event);
}
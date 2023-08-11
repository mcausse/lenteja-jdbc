package io.martung;

@FunctionalInterface
public interface ExceptionableSupplier<T> {

    T get() throws Exception;
}
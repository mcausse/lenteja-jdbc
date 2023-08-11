package io.martung;

@FunctionalInterface
public interface ExceptionableRunnable {

    void run() throws Exception;

}

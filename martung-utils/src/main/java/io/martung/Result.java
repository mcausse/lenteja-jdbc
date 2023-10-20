package io.martung;

import java.util.function.Function;
import java.util.function.Supplier;

public final class Result<T> {

    private final boolean success;
    private final T successResult;
    private final Exception failureException;

    private Result(boolean success, T successResult, Exception failureException) {
        this.success = success;
        this.successResult = successResult;
        this.failureException = failureException;
    }

    public static <T> Result<T> wrapSuccessResult(T successResult) {
        return new Result<>(true, successResult, null);
    }

    public static <T> Result<T> wrapFailureException(Exception failureException) {
        return new Result<>(false, null, failureException);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getSuccessResult() {
        return successResult;
    }

    public Exception getFailureException() {
        return failureException;
    }

    public T getOrElse(Supplier<T> supplier) {
        if (isSuccess()) {
            return getSuccessResult();
        } else {
            return supplier.get();
        }
    }

    public T getOrElseThrow() throws Exception {
        if (isSuccess()) {
            return getSuccessResult();
        } else {
            throw getFailureException();
        }
    }

    public T getOrElseThrow(Function<Exception, RuntimeException> exceptionWrapperException) {
        if (isSuccess()) {
            return getSuccessResult();
        } else {
            throw exceptionWrapperException.apply(getFailureException());
        }
    }
}
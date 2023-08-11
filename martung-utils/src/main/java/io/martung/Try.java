package io.martung;

/**
 * This class is a monad for a computation which may result in an exception.
 * Note that {@link java.util.Optional} is also a monad.
 */
public final class Try {

    private Try() {
    }

    public static <T> Result<T> forResult(ExceptionableRunnable exceptionableRunnable) {
        try {
            exceptionableRunnable.run();
            return Result.wrapSuccessResult(null);
        } catch (Exception e) {
            return Result.wrapFailureException(e);
        }
    }

    public static <T> Result<T> forResult(ExceptionableSupplier<T> supplier) {
        try {
            return Result.wrapSuccessResult(supplier.get());
        } catch (Exception e) {
            return Result.wrapFailureException(e);
        }
    }

}

package org.homs.lentejajdbc;

import java.util.function.Supplier;

public class TransactionalUtils {

    public static void run(DataAccesFacade facade, Runnable r) {
        facade.begin();
        try {
            r.run();
            facade.commit();
        } catch (Exception e) {
            facade.rollback();
            throw new RuntimeException(e);
        }
    }

    public static void runAsReadOnly(DataAccesFacade facade, Runnable r) {
        facade.begin();
        try {
            r.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            facade.rollback();
        }
    }

    public static <T> T runWithReturn(DataAccesFacade facade, Supplier<T> r) {
        facade.begin();
        try {
            var rr = r.get();
            facade.commit();
            return rr;
        } catch (Exception e) {
            facade.rollback();
            throw new RuntimeException(e);
        }
    }

    public static <T> T runAsReadOnlyWithReturn(DataAccesFacade facade, Supplier<T> r) {
        facade.begin();
        try {
            return r.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            facade.rollback();
        }
    }

}

package org.homs.lentejajdbc;

import java.util.function.Supplier;

public class TransactionalOps {

    final DataAccesFacade facade;

    public TransactionalOps(DataAccesFacade facade) {
        this.facade = facade;
    }

    public void run(Runnable r) {
        TransactionalUtils.run(facade, r);
    }

    public void runAsReadOnly(Runnable r) {
        TransactionalUtils.runAsReadOnly(facade, r);
    }

    public <T> T runWithReturn(Supplier<T> r) {
        return TransactionalUtils.runWithReturn(facade, r);
    }

    public <T> T runAsReadOnlyWithReturn(Supplier<T> r) {
        return TransactionalUtils.runAsReadOnlyWithReturn(facade, r);
    }

}

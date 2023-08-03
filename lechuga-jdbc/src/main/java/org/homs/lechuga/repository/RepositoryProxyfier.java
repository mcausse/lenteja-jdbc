//package org.homs.lechuga.repository;
//
//import org.homs.lentejajdbc.DataAccesFacade;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//
//public class RepositoryProxyfier implements InvocationHandler {
//
//    static final Logger LOG = LoggerFactory.getLogger(RepositoryProxyfier.class);
//
//    final DataAccesFacade facade;
//
//    protected RepositoryProxyfier(DataAccesFacade facade) {
//        this.facade = facade;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <E, ID, R extends LechugaRepository<E, ID>> R proxyfy(DataAccesFacade facade, Class<R> repositoryInterface) {
//        return (R) Proxy.newProxyInstance(
//                Thread.currentThread().getContextClassLoader(),// repositoryInterface.getClassLoader(),
//                new Class<?>[]{repositoryInterface},
//                new RepositoryProxyfier(facade)
//        );
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//
////        Method m;
////
////        TransactionalMethod anno = method.getAnnotation(TransactionalMethod.class);
////        if (anno == null) {
////            m = target.getClass().getMethod(method.getName(), method.getParameterTypes());
////            anno = m.getAnnotation(TransactionalMethod.class);
////        }
////
////        if (anno == null) {
////            return method.invoke(target, args);
////        } else if (anno.propagation() == EPropagation.NONE) {
////            return method.invoke(target, args);
////        } else if (anno.propagation() == EPropagation.CREATE_OR_REUSE) {
////            if (facade.isValidTransaction()) {
////                return method.invoke(target, args);
////            } else {
////                return executeInTransaction(method, args, anno.readOnly());
////            }
////        } else if (anno.propagation() == EPropagation.NEW) {
////            return executeInTransaction(method, args, anno.readOnly());
////        } else {
////            throw new JdbcException(anno.propagation().name());
////        }
//        return null;
//    }
//}
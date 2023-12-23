package io.homs.votr;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Container {

    private final Map<Class<?>, Class<?>> definitions = new LinkedHashMap<>();
    private final Map<Class<?>, Object> instances = new LinkedHashMap<>();

    public <T> void register(Class<T> baseType, Class<? extends T> implementationType) {
        if (this.definitions.containsKey(baseType)) {
            throw new RuntimeException("type yet registered: " + baseType.getName());
        }
        this.definitions.put(baseType, implementationType);
    }

    public <T, E extends T> void registerInstance(Class<T> baseType, E implementation) {
        final Class<E> implementationClass = (Class<E>) implementation.getClass();
        register(baseType, implementationClass);
        instances.put(implementationClass, implementation);
    }

    public <T> T resolve(Class<? extends T> forType) {

        final Class<? extends T> type = (Class<? extends T>) definitions.getOrDefault(forType, forType);

        if (instances.containsKey(type)) {
            return (T) instances.get(type);
        }

        return (T) buildNewInstance(type);
    }

    protected Object buildNewInstance(Class<?> type) {

        try {
            if (type.isInterface()) {
                throw new RuntimeException("this interface needs to be registered: " + type.getName());
            }

            Constructor<?>[] ctors = type.getDeclaredConstructors();
            if (ctors.length == 0) {
                throw new RuntimeException("no C'tor found for class: " + type.getName());
            }
            // selecciona al C'tor amb més arguments
            Constructor<?> longestCtor = getLongestCtor(ctors);

            // resol les dependències (arguments del c'tor)
            Object[] parameterValues = resolveParameterValues(longestCtor);

            // instancia
            Object r = longestCtor.newInstance(parameterValues);

            this.instances.put(type, r);

            return r;
        } catch (Exception e) {
            throw new RuntimeException("creating new instance: " + type.getName(), e);
        }
    }

    protected Object[] resolveParameterValues(Constructor<?> longestCtor) throws Exception {
        Object[] parameterValues;
        parameterValues = new Object[longestCtor.getParameters().length];
        for (int i = 0; i < longestCtor.getParameters().length; i++) {
            Parameter parameter = longestCtor.getParameters()[i];
            Object paramValue = resolve(parameter.getType());
            parameterValues[i] = paramValue;
        }
        return parameterValues;
    }

    private static Constructor<?> getLongestCtor(Constructor<?>[] ctors) {
//        return Stream.of(ctors).reduce(ctors[0], (c1, c2) -> c1.getParameterCount() > c2.getParameterCount() ? c1 : c2);
        int maxCtorNumArgs = -1;
        int maxCtorNumArgsIndex = -1;
        for (int i = 0; i < ctors.length; i++) {
            int ctorNumArgs = ctors[i].getParameterCount();
            if (maxCtorNumArgs < ctorNumArgs) {
                maxCtorNumArgs = ctorNumArgs;
                maxCtorNumArgsIndex = i;
            }
        }
        return ctors[maxCtorNumArgsIndex];
    }
}

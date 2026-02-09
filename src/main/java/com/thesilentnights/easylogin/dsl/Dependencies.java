package com.thesilentnights.easylogin.dsl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Dependencies {
    private static final Map<Class<?>, Object> SINGLETONS = new ConcurrentHashMap<>();

    private Dependencies() {
    }


    public static <T> void register(Class<T> type, Supplier<T> factory) {
        if (type == null || factory == null) {
            throw new IllegalArgumentException("Type and factory must not be null");
        }
        SINGLETONS.computeIfAbsent(type, k -> factory.get());
    }

    public static <T> void register(Class<T> type, T instance) {
        if (type == null || instance == null) {
            throw new IllegalArgumentException("Type and instance must not be null");
        }
        SINGLETONS.putIfAbsent(type, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }
        T bean = (T) SINGLETONS.get(type);
        if (bean == null) {
            throw new IllegalStateException("No singleton bean registered for type: " + type.getName());
        }
        return bean;
    }

    public static boolean containsBean(Class<?> type) {
        return SINGLETONS.containsKey(type);
    }

}

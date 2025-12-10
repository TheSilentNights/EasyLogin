package com.thesilentnights.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerService {
    private static final Map<String, Long> entries = new HashMap<>();

    public static boolean contains(String identifier) {
        return entries.get(identifier) != null && System.currentTimeMillis() < entries.get(identifier);
    }

    public static void cleanExpired() {
        entries.entrySet().removeIf(entry -> System.currentTimeMillis() > entry.getValue());
    }

    public static String generateIdentifier(UUID uuid, Class<?> serviceClass) {
        return uuid + "_" + serviceClass.getName();

    }

    public static void add(String identifier, long duration) {
        entries.put(identifier, System.currentTimeMillis() + duration);
    }

    public static void clear() {
        entries.clear();
    }
}

package com.thesilentnights.easylogin.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerService {

    private static final Map<String, Long> entries = new HashMap<>();

    public static boolean contains(String identifier) {
        Long expireTime = entries.get(identifier);
        return expireTime != null && System.currentTimeMillis() < expireTime;
    }

    public static String generateIdentifier(UUID uuid, Class<?> serviceClass) {
        return uuid.toString() + "_" + serviceClass.getSimpleName();
    }

    public static void add(String identifier, long duration) {
        entries.put(identifier, System.currentTimeMillis() + duration);
    }

    public void cleanExpired() {
        entries.entrySet().removeIf(entry -> System.currentTimeMillis() > entry.getValue());
    }

    public void clear() {
        entries.clear();
    }
}
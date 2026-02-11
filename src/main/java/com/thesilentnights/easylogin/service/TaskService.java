package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.service.task.Task;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskService {

    private static final ConcurrentHashMap<String, Task> taskMap = new ConcurrentHashMap<>();


    public static void cancelPlayer(UUID uuid) {
        taskMap.entrySet().removeIf(entry -> entry.getKey().startsWith(uuid.toString()));
    }

    public static void tick() {
        if (taskMap.isEmpty()) {
            return;
        }
        taskMap.values().forEach(Task::tick);
    }

    public static void addTask(String identifier, Task task) {
        taskMap.put(identifier, task);
    }

    public static String generateTaskIdentifier(UUID uuid, String name) {
        return uuid.toString() + "_" + name;
    }

    public static void cancelTask(String identifier) {
        taskMap.remove(identifier);
    }

    public Task getTask(String identifier) {
        return taskMap.get(identifier);
    }

    public enum Suffix {
        MESSAGE,
        TIMEOUT
    }
}
package com.thesilentnights.service;

import com.thesilentnights.service.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskService {
    private static final Map<String, Task> taskMap = new HashMap<>();

    public static void addTask(String identifier, Task task) {
        taskMap.put(identifier, task);
    }

    public static Task getTask(String identifier) {
        return taskMap.get(identifier);
    }

    public static String generateTaskIdentifier(UUID uuid, TaskType taskType) {
        return uuid + "_" + taskType;
    }

    public static void tick() {
        if (taskMap.isEmpty()) {
            return;
        }
        taskMap.values().forEach(Task::tick);
    }

    public static void cancelTask(String identifier) {
        taskMap.remove(identifier);
    }

    public static void cancelPlayer(UUID uuid) {
        taskMap.entrySet().removeIf(entry -> entry.getKey().startsWith(uuid.toString()));
    }

    public enum TaskType {
        KICK_PLAYER, MESSAGE
    }


}
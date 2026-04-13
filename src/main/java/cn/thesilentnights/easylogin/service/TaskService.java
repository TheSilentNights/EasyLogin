package cn.thesilentnights.easylogin.service;

import java.util.TreeSet;
import java.util.UUID;

import cn.thesilentnights.easylogin.service.task.Loop;
import cn.thesilentnights.easylogin.service.task.Task;

public class TaskService {
    private static final TreeSet<Task> taskQueue = new TreeSet<>();

    public static void addTask(Task task) {
        taskQueue.add(task);
    }

    public static void cancelPlayer(UUID uuid) {
        taskQueue.removeIf(task -> task.shouldCancel(uuid));
    }

    public static void tick() {
        if (taskQueue.isEmpty()) {
            return;
        }

        Task firstTask = taskQueue.first();
        if (System.currentTimeMillis() >= firstTask.getEndTimeMillis()) {
            taskQueue.pollFirst();
            firstTask.execute();

            if (firstTask instanceof Loop loop) {
                addTask(loop.regenerate());
            }
        }
    }
}
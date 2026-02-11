package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.service.task.Loop;
import com.thesilentnights.easylogin.service.task.Task;

import java.util.TreeSet;
import java.util.UUID;

public class TaskService {
    static TreeSet<Task> tasks = new TreeSet<>();
    static int nextHook = 0;
    static int timePassedAfterPrepare = 0;

    public static void addTask(Task task) {
        reduceTickDelay();
        tasks.add(task);
        prepareNextTask();
    }

    public static void cancelPlayer(UUID uuid) {
        tasks.removeIf(task -> task.shouldCancel(uuid));
    }

    private static void reduceTickDelay() {
        tasks.forEach(task -> task.reduceTickDelay(timePassedAfterPrepare));
    }

    private static void prepareNextTask() {
        timePassedAfterPrepare = 0;
        if (tasks.isEmpty()) {
            return;
        }
        nextHook = tasks.first().getTickDelay();
    }

    public static void tick() {
        if (tasks.isEmpty()) {
            return;
        }
        timePassedAfterPrepare++;
        if (timePassedAfterPrepare > nextHook) {
            Task task = tasks.pollFirst();
            //tasks is not empty
            assert task != null;
            task.execute();

            //if is Loop task readd
            if (task instanceof Loop loop) {
                addTask(loop.regenerate());
            } else {
                reduceTickDelay();
                prepareNextTask();
            }
        }
    }


}
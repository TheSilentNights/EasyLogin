package cn.thesilentnights.easylogin.services.task;

import java.util.PriorityQueue;
import java.util.UUID;

public class TaskService {
        private static final PriorityQueue<Task> taskQueue = new PriorityQueue<>();

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

                Task firstTask = taskQueue.peek();
                if (System.currentTimeMillis() >= firstTask.getEndTimeMillis()) {
                        taskQueue.poll();
                        firstTask.execute();

                        if (firstTask instanceof Loop loop) {
                                addTask(loop.regenerate());
                        }
                }
        }
}

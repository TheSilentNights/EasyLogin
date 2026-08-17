package cn.thesilentnights.easylogin.services.task;

import java.util.UUID;

public interface TaskService {
        void addTask(Task task);

        void cancelPlayer(UUID uuid);

        void tick();
}

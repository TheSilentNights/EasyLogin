package cn.thesilentnights.easylogin.service.task;

import java.util.UUID;

public abstract class Task implements Comparable<Task> {
    //the logic when timeTick reaches
    public abstract void execute();

    public abstract Long getEndTimeMillis();

    public abstract boolean shouldCancel(UUID uuid);

    @Override
    public int compareTo(Task o) {
        return (this.getEndTimeMillis() - o.getEndTimeMillis()) > 0 ? 1 : -1;
    }
}

package com.thesilentnights.easylogin.service.task;

import java.util.UUID;

public abstract class Task implements Comparable<Task> {
    //the logic when timeTick reaches
    public abstract void execute();

    public abstract int getTickDelay();

    public abstract void reduceTickDelay(int tickDelay);

    public abstract boolean shouldCancel(UUID uuid);

    @Override
    public int compareTo(Task o) {
        return this.getTickDelay() - o.getTickDelay();
    }
}

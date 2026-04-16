package cn.thesilentnights.easylogin.service.task;

import java.util.Objects;
import java.util.UUID;

public abstract class Task implements Comparable<Task> {
    //the logic when timeTick reaches
    public abstract void execute();

    public abstract Long getEndTimeMillis();

    public abstract boolean shouldCancel(UUID uuid);

    @Override
    public int compareTo(Task o) {
        return Long.compare(this.getEndTimeMillis(), o.getEndTimeMillis());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
}

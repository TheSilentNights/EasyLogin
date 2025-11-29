package com.thesilentnights.task;

import java.util.UUID;

public interface TickTimer {
    void tick();

    UUID getUUId();

    TickType getTickType();

    static enum TickType {
        MESSAGE, KICK
    }
}

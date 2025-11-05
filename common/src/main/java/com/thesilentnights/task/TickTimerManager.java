package com.thesilentnights.task;

import java.util.LinkedList;
import java.util.UUID;

public class TickTimerManager {
    static LinkedList<TickTimer> tickTimers = new LinkedList<>();
    public static void addTickTimer(TickTimer tickTimer){
        tickTimers.add(tickTimer);
    }
    public static void tick(){
        for (TickTimer tickTimer : tickTimers) {
            tickTimer.tick();
        }
    }

    public static void cancel(UUID name){
        for (TickTimer tickTimer : tickTimers) {
            if (tickTimer.getUUId().equals(name)){
                tickTimers.remove(tickTimer);
                break;
            }
        }
    }
}

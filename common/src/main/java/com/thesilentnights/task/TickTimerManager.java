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

    public static void cancel(UUID uuid, TickTimer.TickType tickType){
        for (TickTimer tickTimer : tickTimers) {
            if (tickTimer.getUUId().equals(uuid) && tickTimer.getTickType() == tickType){
                tickTimers.remove(tickTimer);
                break;
            }
        }
    }

    public static void cancelPlayer(UUID uuid){
        for (TickTimer tickTimer : tickTimers) {
            if (tickTimer.getUUId().equals(uuid)){
                tickTimers.remove(tickTimer);
                break;
            }
        }
    }


}

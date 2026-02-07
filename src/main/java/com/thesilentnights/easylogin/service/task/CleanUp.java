package com.thesilentnights.easylogin.service.task;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CleanUp implements Task {

    private final long delay;
    private final List<Consumer<Void>> cleanUpMethods = new LinkedList<>();
    private long tickCount = 0;

    public CleanUp(long delay) {
        this.delay = delay;
    }

    public void addCleanUpMethod(Consumer<Void> method) {
        if (method != null) {
            cleanUpMethods.add(method);
        }
    }

    @Override
    public void tick() {
        tickCount++;
        if (tickCount >= delay) {
            tickCount = 0;
            for (Consumer<Void> method : cleanUpMethods) {
                method.accept(null);
            }
        }
    }
}
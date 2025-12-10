package com.thesilentnights.service.task;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.function.Consumer;

@Slf4j
public class CleanUp implements Task {
    long delay;
    long tickCount = 0;
    LinkedList<Consumer<Void>> cleanUpMethods = new LinkedList<>();

    public void addCleanUpMethods(Consumer<Void> method) {
        cleanUpMethods.add(method);
    }

    public CleanUp(long delay) {
        this.delay = delay;
    }

    @Override
    public void tick() {
        this.tickCount++;
        if (this.tickCount >= this.delay) {
            this.tickCount = 0;
            cleanUpMethods.forEach(method -> {
                method.accept(null);
            });
        }
    }
}

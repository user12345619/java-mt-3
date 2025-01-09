package org.example.tunnel.train;

import java.util.concurrent.atomic.AtomicInteger;

public enum TrainIdGenerator {
    INSTANCE;

    private final AtomicInteger id = new AtomicInteger();

    public int generateId() {
        return id.getAndIncrement();
    }
}

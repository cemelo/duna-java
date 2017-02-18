package io.duna.core;

public class DunaOptions {

    private int eventLoops;
    private int workers;

    public int getEventLoops() {
        return eventLoops;
    }

    public DunaOptions eventLoops(int eventLoops) {
        this.eventLoops = eventLoops;
        return this;
    }

    public int getWorkers() {
        return workers;
    }

    public DunaOptions workers(int workers) {
        this.workers = workers;
        return this;
    }
}

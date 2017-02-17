package io.duna.core;

public class DunaOptions {

    private int eventLoopThreads;
    private int workerThreads;
    private boolean useCommonWorkerPool;

    public int getEventLoopThreads() {
        return eventLoopThreads;
    }

    public DunaOptions eventLoopThreads(int eventLoopThreads) {
        this.eventLoopThreads = eventLoopThreads;
        return this;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public DunaOptions workerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
        return this;
    }

    public boolean isUseCommonWorkerPool() {
        return useCommonWorkerPool;
    }

    public DunaOptions useCommonWorkerPool(boolean useCommonWorkerPool) {
        this.useCommonWorkerPool = useCommonWorkerPool;
        return this;
    }
}

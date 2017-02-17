package io.duna.core2.concurrent.manager;

import io.duna.core2.function.Consumer;

import java.util.concurrent.ExecutorService;

class ConsumerExecutionInstance {

    private Consumer<?> consumer;
    private ExecutorService executor;
    private int cost;
    private boolean blocking;

    public Consumer<?> getConsumer() {
        return consumer;
    }

    public ConsumerExecutionInstance consumer(Consumer<?> consumer) {
        this.consumer = consumer;
        return this;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public ConsumerExecutionInstance executor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public ConsumerExecutionInstance cost(int cost) {
        this.cost = cost;
        return this;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public ConsumerExecutionInstance blocking(boolean blocking) {
        this.blocking = blocking;
        return this;
    }
}

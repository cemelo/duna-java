package io.duna.core.concurrent.consumer;

public class ConsumerOptions {

    private boolean blocking;
    private int instances;
    private int cost;

    public boolean isBlocking() {
        return blocking;
    }

    public ConsumerOptions blocking(boolean blocking) {
        this.blocking = blocking;
        return this;
    }

    public int getInstances() {
        return instances;
    }

    public ConsumerOptions instances(int instances) {
        this.instances = instances;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public ConsumerOptions cost(int cost) {
        this.cost = cost;
        return this;
    }
}

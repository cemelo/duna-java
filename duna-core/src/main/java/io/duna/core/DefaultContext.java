package io.duna.core;

public class DefaultContext implements Context {

    private final Duna owner;

    public DefaultContext(Duna owner) {
        this.owner = owner;
    }

    @Override
    public Duna currentManager() {
        return owner;
    }
}

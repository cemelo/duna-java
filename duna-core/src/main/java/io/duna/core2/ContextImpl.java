package io.duna.core2;

public class ContextImpl implements Context {

    private Duna manager;

    public ContextImpl(Duna manager) {
        this.manager = manager;
    }

    @Override
    public Duna manager() {
        return null;
    }
}

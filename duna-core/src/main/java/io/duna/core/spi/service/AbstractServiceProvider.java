package io.duna.core.spi.service;

import io.duna.core.Duna;
import io.duna.core.eventbus.EventBus;

public abstract class AbstractServiceProvider implements ServiceProvider {

    private Duna duna;
    private EventBus eventBus;

    @Override
    public EventBus eventBus() {
        return duna.eventBus();
    }

    public void setDuna(Duna duna) {
        this.duna = duna;
    }
}

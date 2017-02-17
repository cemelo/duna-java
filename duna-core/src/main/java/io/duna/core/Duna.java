package io.duna.core;

import io.duna.core.eventbus.EventBus;

public interface Duna {

    void start();

    EventBus eventBus();

    HandlerManager handlerManager();

    static Duna create(DunaOptions options) {
        return new DunaImpl(options);
    }

}

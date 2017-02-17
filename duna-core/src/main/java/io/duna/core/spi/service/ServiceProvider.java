package io.duna.core.spi.service;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.EventBus;

public interface ServiceProvider {

    void start(Future<Void> future);

    void stop(Future<Void> future);

    EventBus eventBus();

    void setEventBus(EventBus eventBus);

}

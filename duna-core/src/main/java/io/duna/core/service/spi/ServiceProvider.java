package io.duna.core.service.spi;

import io.duna.core.concurrent.Future;

public interface ServiceProvider {

    void start(Future<Void> startFuture);

    void stop(Future<Void> stopFuture);
}

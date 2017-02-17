package io.duna.core2.service.spi;

import io.duna.core2.concurrent.future.Future;

public interface ServiceProvider {

    void start(Future<Void> startFuture);

    void stop(Future<Void> stopFuture);
}

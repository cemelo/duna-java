package io.duna.core;

import io.duna.core.concurrent.future.Future;

public interface Duna {

    Future<Void> start();

    Future<Void> shutdown();

}

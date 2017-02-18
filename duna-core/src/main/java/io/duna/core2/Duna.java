package io.duna.core2;

import io.duna.core.eventbus.EventBus;
import io.duna.core2.concurrent.future.Future;

import java.util.Optional;

public interface Duna {

    Future<Void> start();

    Future<Void> shutdown();
    
}

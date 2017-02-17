package io.duna.core2;

import io.duna.core.eventbus.EventBus;
import io.duna.core2.concurrent.future.Future;

import java.util.Optional;

public interface Duna {

    Future<Void> start();

    Future<Void> shutdown();

    static Optional<EventBus> eventBus() {
        Optional<Context> context = context();

        if (context.isPresent())
            return Optional.of(((DunaImpl) context().get().manager()).eventBus());
        else
            return Optional.empty();
    }

    static Optional<Context> context() {
        return Optional.ofNullable(DunaImpl.context());
    }
}

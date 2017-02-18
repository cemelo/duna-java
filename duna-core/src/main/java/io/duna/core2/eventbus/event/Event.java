package io.duna.core2.eventbus.event;

import io.duna.core2.function.Consumer;

import java.util.function.Predicate;

public interface Event<T> {

    String getName();

    Event<T> withCost(int cost);

    Event<T> withFilter(Predicate<? super T> predicate);

    Event<T> withInterceptor(Consumer<T> interceptor);

}

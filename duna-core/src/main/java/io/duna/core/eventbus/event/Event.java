package io.duna.core.eventbus.event;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Event<T> {

    String getName();

    int getCost();

    Predicate<T> getFilter();

    Consumer<T> getInterceptor();

    Event<T> setCost(int cost);

    Event<T> setFilter(Predicate<T> predicate);

    Event<T> setInterceptor(Consumer<T> interceptor);

}

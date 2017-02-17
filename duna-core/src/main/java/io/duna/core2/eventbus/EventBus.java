package io.duna.core2.eventbus;

public interface EventBus {

    <T> Event<T> event(String name);

    void dispatch(Event<?> event);

}

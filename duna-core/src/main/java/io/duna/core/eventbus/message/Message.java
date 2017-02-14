package io.duna.core.eventbus.message;

import io.duna.core.eventbus.Context;
import io.duna.core.eventbus.event.Event;

import java.util.Set;

public interface Message<T> {

    String source();

    String target();

    Context headers();

    Set<T> attachments();

    <V> Event<V> reply();

    boolean failed();

    String cause();
}

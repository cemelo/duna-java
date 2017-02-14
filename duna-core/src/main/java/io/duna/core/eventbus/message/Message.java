package io.duna.core.eventbus.message;

import io.duna.core.eventbus.Event;
import org.eclipse.collections.api.multimap.Multimap;

public interface Message<T> {

    String source();

    String target();

    String returnAddress();

    Multimap<String, String> headers();

    T body();

    String cause();

    boolean succeeded();

    boolean failed();

    <V> Event<V> reply();
}

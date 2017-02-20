package io.duna.core.eventbus;

import org.eclipse.collections.api.multimap.Multimap;

public interface Message<T> {

    String getSource();

    String getTarget();

    String getResponseEvent();

    Multimap<String, String> getHeaders();

    T getBody();

    Throwable getCause();

    boolean succeeded();

    boolean failed();

    <V> void reply(V response);

    void fail(Throwable t);

}

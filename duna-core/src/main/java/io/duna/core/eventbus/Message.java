package io.duna.core.eventbus;

import org.eclipse.collections.api.multimap.Multimap;

public interface Message2<T> {

    String getSource();

    String getTarget();

    String getRespondTo();

    Multimap<String, String> getHeaders();

    T getBody();

    Throwable getCause();

    boolean succeeded();

    boolean failed();

}

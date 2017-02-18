package io.duna.core.eventbus;

import java.util.Map;

public interface Message<T> {

    String getSource();

    String getResponseEvent();

    Map<String, String> getHeaders();

    T getBody();

    Throwable getCause();

    boolean succeeded();

    boolean failed();
}

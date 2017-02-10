package io.duna.core.net;

import org.eclipse.collections.api.map.ImmutableMap;

public interface Envelope<T> {

    String source();

    String target();

    ImmutableMap<String, String> headers();

    T body();
}

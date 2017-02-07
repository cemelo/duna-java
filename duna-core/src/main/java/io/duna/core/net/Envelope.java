package io.duna.core.net;

import java.util.Map;

public interface Envelope<T> {

    String source();

    String target();

    Map<String, String> headers();

    T body();
}

package io.duna.core.net;

import org.eclipse.collections.api.multimap.ImmutableMultimap;

public interface Envelope<T> {

    String source();

    String target();

    ImmutableMultimap<String, String> headers();

    T body();
}

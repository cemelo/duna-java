package io.duna.core2.function;

import io.duna.core2.util.tuple.Pair;

@FunctionalInterface
public interface BiConsumer<T, V> extends Consumer<Pair<T, V>> {

    void accept(T first, V second);

    default void accept(Pair<T, V> offer) {
        accept(offer.getFirst(), offer.getSecond());
    }
}

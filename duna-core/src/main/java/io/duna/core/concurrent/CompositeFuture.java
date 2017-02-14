package io.duna.core.concurrent;

import java.util.List;

public interface CompositeFuture extends Future<CompositeFuture> {

    List<Future<?>> list();

    int size();

    <T> T getAt(int index);

}

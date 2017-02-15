package io.duna.core.concurrent.future;

import java.util.List;

public interface CompositeFuture extends Future<CompositeFuture> {

    List<Future<?>> list();

    int size();

    <T> T getAt(int index);

}

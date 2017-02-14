package io.duna.core.eventbus.impl;

import io.duna.core.function.Handler;
import org.eclipse.collections.api.bimap.BiMap;

public class EventRouter {

    private BiMap<String, Handler<?>> handlerNames;

    public void dispatch(String event) {

    }

    <T> void registerHandler(String address, Handler<T> handler) {

    }
}

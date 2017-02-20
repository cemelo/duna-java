package io.duna.core;

import io.duna.core.internal.ContextImpl;

import java.util.Map;

public interface Context {

    Duna manager();

    <T> T get(String key);

    <T> void put(String key, T value);

    <T> void putAll(Map<String, T> data);

    void remove(String key);

    static Context currentContext() {
        return ContextImpl.currentContext();
    }
}

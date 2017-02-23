package io.duna.core.internal;

import io.duna.core.Context;
import io.duna.core.Manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextImpl implements Context {

    private static final ThreadLocal<Context> contextHolder = new ThreadLocal<>();

    private Manager manager;
    private Map<String, Object> data;

    public ContextImpl(Manager manager) {
        this.manager = manager;
        this.data = new ConcurrentHashMap<>();
    }

    public static Context currentContext() {
        return contextHolder.get();
    }

    public static void setContext(Context context) {
        contextHolder.set(context);
    }

    @Override
    public Manager manager() {
        return this.manager;
    }

    @Override
    public <T> T get(String key) {
        return (T) data.get(key);
    }

    @Override
    public <T> void put(String key, T value) {
        data.put(key, value);
    }

    @Override
    public <T> void putAll(Map<String, T> data) {
        this.data.putAll(data);
    }

    @Override
    public void remove(String key) {
        data.remove(key);
    }
}

package io.duna.core.eventbus;

import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class Context {

    private ConcurrentHashMap<String, ConcurrentSkipListSet<String>> headers;

    public Context() {
        headers = ConcurrentHashMap.newMap();
    }

    public Set<String> get(String key) {
        return headers.computeIfAbsent(key, k -> new ConcurrentSkipListSet<>());
    }

    public Context put(String key, String value) {
        get(key).add(value);
        return this;
    }

    public Context put(String key, String ... values) {
        Arrays.stream(values)
            .forEach(get(key)::add);
        return this;
    }

    public Context put(String key, Collection<String> values) {
        get(key).addAll(values);
        return this;
    }
}

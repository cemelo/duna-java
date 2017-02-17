package io.duna.core;

public interface Context {

    static Context currentContext() {
        // return ThreadLocal
        return null;
    }

    Duna currentManager();
}

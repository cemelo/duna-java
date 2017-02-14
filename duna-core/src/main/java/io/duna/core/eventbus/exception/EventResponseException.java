package io.duna.core.eventbus.exception;

import org.jetbrains.annotations.NonNls;

public class EventResponseException extends RuntimeException {
    public EventResponseException() {
    }

    public EventResponseException(@NonNls String s) {
        super(s);
    }

    public EventResponseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EventResponseException(Throwable throwable) {
        super(throwable);
    }

    public EventResponseException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}

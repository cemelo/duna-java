package io.duna.core.concurrent;

import io.duna.core.Context;
import org.jetbrains.annotations.NonNls;

public class DunaThread extends Thread {

    private Context context;

    public DunaThread(Runnable runnable, @NonNls String s) {
        super(runnable, s);
    }

    public static void setContext(Context context) {
        DunaThread currentThread = currentDunaThread();
        if (currentThread != null) {
            currentThread.context = context;
        } else {
            throw new IllegalStateException("Contexts can only be attributed to Duna threads.");
        }
    }

    public static DunaThread currentDunaThread() {
        if (Thread.currentThread() instanceof DunaThread) {
            return (DunaThread) Thread.currentThread();
        } else {
            return null;
        }
    }

    public Context getContext() {
        return context;
    }
}

/*
 * Copyright 2016 higherfrequencytrading.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.duna.core.internal.concurrent;

import io.duna.core.Context;
import io.duna.core.Duna;
import io.duna.core.internal.ContextImpl;
import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadFactory;

public class DunaAffinityThreadFactory implements ThreadFactory {

    private final String name;
    private final boolean daemon;
    private final Duna manager;
    @NotNull
    private final AffinityStrategy[] strategies;
    @Nullable
    private AffinityLock lastAffinityLock = null;
    private int id = 1;

    public DunaAffinityThreadFactory(Duna manager, AffinityStrategy... strategies) {
        this(manager, false, strategies);
    }

    public DunaAffinityThreadFactory(Duna manager, boolean daemon, @NotNull AffinityStrategy... strategies) {
        this.name = "duna";
        this.manager = manager;
        this.daemon = daemon;
        this.strategies = strategies.length == 0 ? new AffinityStrategy[]{AffinityStrategies.ANY} : strategies;
    }

    @NotNull
    @Override
    public synchronized Thread newThread(@NotNull final Runnable r) {
        Context context = new ContextImpl(manager);

        String name2 = id <= 1 ? name : (name + '-' + id);
        id++;
        Thread t = new Thread(() -> {
            AffinityLock al = lastAffinityLock == null ? AffinityLock.acquireLock() : lastAffinityLock.acquireLock(strategies);
            try {
                if (al.cpuId() >= 0)
                    lastAffinityLock = al;


                r.run();
            } finally {
                al.release();
            }
        }, name2);
        t.setDaemon(daemon);
        return t;
    }
}

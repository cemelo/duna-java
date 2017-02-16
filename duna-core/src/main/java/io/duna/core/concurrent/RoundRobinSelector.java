package io.duna.core.concurrent;

import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.multimap.list.SynchronizedPutFastListMultimap;

import java.util.Comparator;
import java.util.Set;

public class RoundRobinSelector<E> {

    private final MutableListMultimap<Integer, E> countedElements;

    public RoundRobinSelector(Set<E> elements) {
        this.countedElements = SynchronizedPutFastListMultimap.newMultimap();
        elements.forEach(i -> countedElements.put(0, i));
    }

    public E next() {
        Pair<Integer, E> pair;

        synchronized (countedElements) {
            pair = countedElements
                .keyValuePairsView()
                .min(Comparator.comparingInt(Pair::getOne));

            countedElements.remove(pair.getOne(), pair.getTwo());
            countedElements.put(pair.getOne() + 1, pair.getTwo());
        }

        return pair.getTwo();
    }

    public void add(E element) {
        countedElements.put(0, element);
    }

    public void remove(E element) {
        synchronized (countedElements) {
            countedElements
                .forEachKeyValue((k, v) -> {
                    if (v.equals(element))
                        countedElements.remove(k, v);
                });
        }
    }
}

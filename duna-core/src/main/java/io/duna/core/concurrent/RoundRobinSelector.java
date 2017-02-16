package io.duna.core.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class RoundRobinSelector<E> {

    private final NavigableSet<WeightedElement> elements;

    public RoundRobinSelector(Set<E> elements) {
        this.elements = new ConcurrentSkipListSet<>();
        elements.forEach(e -> this.elements.add(new WeightedElement(e, 0)));
    }

    public E next() {
        WeightedElement current = elements.pollFirst();
        current.weight++;

        elements.add(current);

        return current.element;
    }

    public void add(E element) {
        elements.add(new WeightedElement(element, 0));
    }

    public void remove(E element) {
        elements.removeIf(w -> w.element.equals(element));
    }

    private class WeightedElement implements Comparable<WeightedElement> {
        E element;
        int weight;

        private WeightedElement(E element, int weight) {
            this.element = element;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WeightedElement that = (WeightedElement) o;
            return weight == that.weight &&
                Objects.equals(element, that.element);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element, weight);
        }

        @Override
        public int compareTo(@NotNull WeightedElement o) {
            return this.weight - o.weight;
        }
    }
}

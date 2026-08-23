package com.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stores elements of one type selected by the caller.
 *
 * @param <T> the type of elements stored in this object
 */
public class GenericStore<T> {
    private final List<T> items = new ArrayList<>();

    /**
     * Adds one element to this store.
     *
     * @param item the element to add
     */
    public void add(T item) {
        items.add(
            Objects.requireNonNull(
                item,
                "item must not be null"));
    }

    /**
     * Returns the element at the requested zero-based index.
     *
     * @param index the zero-based element index
     * @return the element stored at the index
     */
    public T get(int index) {
        return items.get(index);
    }

    /**
     * Returns the number of stored elements.
     *
     * @return the current number of elements
     */
    public int size() {
        return items.size();
    }

    /**
     * Returns an unmodifiable snapshot of the stored elements.
     *
     * @return an unmodifiable List snapshot
     */
    public List<T> getAll() {
        return List.copyOf(items);
    }
}
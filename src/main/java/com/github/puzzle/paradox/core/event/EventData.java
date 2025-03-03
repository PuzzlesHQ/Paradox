package com.github.puzzle.paradox.core.event;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
 * CODE FROM FABRIC MODIFIED, ORIGINAL LICENSE:
 * the Apache License, Version 2.0
 *
 * Modified by repletsin5
 */
@Deprecated(since = "1.2.11", forRemoval = true)
class EventData<T>  {
    ArrayList<T> listeners;

    @SuppressWarnings("unchecked")
    EventData( Class<?> listenerClass) {
        this.listeners = new ArrayList<>(List.of((T[]) Array.newInstance(listenerClass, 0)));

    }

    void addListener(T listener) {
        listeners.add(listener);
    }
    void removeListener(T listener) {
        listeners.remove(listener);
    }
}
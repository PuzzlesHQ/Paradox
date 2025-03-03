package com.github.puzzle.paradox.core.event;

/*
 * CODE FROM FABRIC MODIFIED, ORIGINAL LICENSE:
 * the Apache License, Version 2.0
 *
 * Modified by repletsin5
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated(since = "1.2.11", forRemoval = true)
public abstract class Event<T> {
    protected volatile T invoker;

    public final T invoker() {
        return invoker;
    }

    public abstract void subscribe(T listener);
    public abstract void unsubscribe(T listener);
}
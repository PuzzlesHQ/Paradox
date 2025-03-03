package com.github.puzzle.paradox.core.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
@SuppressWarnings({"removal"})
@Deprecated(since = "1.2.11", forRemoval = true)
public class RunnableArrayListEvent extends Event<Runnable> {

    private final Object lock = new Object();
    private final List<Runnable> runners;

    public RunnableArrayListEvent() {
        runners = new ArrayList<>();
        invoker = () -> runners.forEach(Runnable::run);
    }

    @Override
    public void subscribe(Runnable listener) {
        Objects.requireNonNull(listener, "Tried to register a null listener!");
        synchronized (lock) {
            runners.add(listener);
        }
    }

    @Override
    public void unsubscribe(Runnable listener) {
        Objects.requireNonNull(listener, "Tried to unregister a null listener!");
        synchronized (lock) {
            runners.remove(listener);
        }
    }

    public List<Runnable> getRunnables() {
        return Collections.unmodifiableList(runners);
    }
}
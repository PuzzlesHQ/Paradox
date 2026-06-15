package com.github.puzzle.paradox.loader.entrypoint;


import com.github.puzzle.paradox.loader.lang.AnyObject;
import com.github.puzzle.paradox.loader.lang.ILangProvider;
import com.github.puzzle.paradox.loader.plugin.AdapterPathPair;
import com.github.puzzle.paradox.loader.plugin.PluginContainer;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.security.ProviderException;
import java.util.*;
import java.util.function.Consumer;

public class EntrypointContainer {

    private static final Logger LOGGER = LogManager.getLogger("Puzzle Entrypoint Containter");

    private final ImmutableMap<String, ImmutableCollection<AdapterPathPair>> entrypointClasses;
    private final PluginContainer container;

    private static final Map<String, Object> ENTRYPOINT_OBJECT_INSTANCES = new HashMap<>();
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public <T> void invokeClasses(String key, Class<T> type, Consumer<? super T> invoker) throws Exception {
        ILangProvider.init();

        List<Object> objectList = new ArrayList<>();

        if (entrypointClasses.get(key) != null) {
            for (AdapterPathPair pair : Objects.requireNonNull(entrypointClasses.get(key))){
                String adapter = pair.getAdapter();
                String className = pair.getValue();
                Object instance = ENTRYPOINT_OBJECT_INSTANCES.get(className);

                if (instance != null) {
                    objectList.add(instance);
                    if(type.isAssignableFrom(instance.getClass())){
                        invoker.accept((T) instance);
                    }
                    continue;
                }

                ILangProvider provider = ILangProvider.PROVDERS.get(adapter);

                try {
                    Object newInstance = provider.create(container.INFO, className, AnyObject.class);

                    objectList.add(newInstance);
                    ENTRYPOINT_OBJECT_INSTANCES.put(className, newInstance);
                    if(type.isAssignableFrom(newInstance.getClass())){
                        invoker.accept((T) newInstance);
                    }
                } catch (RuntimeException ignored) {
                    LOGGER.warn(ignored);
                    warnClass(className, adapter);
                }
            }
        }

    }

    public EntrypointContainer(PluginContainer container, @NotNull ImmutableMap<String, ImmutableCollection<AdapterPathPair>> entrypoints) {
        this.container = container;
        entrypointClasses = entrypoints;
    }
    private static void warnClass(String className, String adapter) {
        LOGGER.warn("Could not instantiate entrypoint class: \"{}\" for adapter: \"{}\", skipping either broken class or class from wrong side.", className, adapter);
    }

}

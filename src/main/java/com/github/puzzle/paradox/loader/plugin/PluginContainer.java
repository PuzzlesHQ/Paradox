package com.github.puzzle.paradox.loader.plugin;

import com.github.puzzle.paradox.api.records.Version;
import com.github.puzzle.paradox.loader.entrypoint.EntrypointContainer;
import com.github.puzzle.paradox.loader.plugin.info.PluginInfo;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.zip.ZipFile;

public class PluginContainer {
    public PluginInfo INFO;
    private final EntrypointContainer entrypointContainer;

    public final String NAME;
    public final String ID;
    public final Version VERSION;
    public final ZipFile JAR;

    public PluginContainer(PluginInfo info) {
        this(info, null);
    }

    public PluginContainer(@NotNull PluginInfo info, ZipFile jar) {
        this.INFO = info;
        this.entrypointContainer = new EntrypointContainer(this, info.Entrypoints);

        NAME = info.DisplayName;
        ID = info.ID;
        VERSION = info.PlVersion;
        JAR = jar;
    }

    public <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) throws Exception {
        entrypointContainer.invokeClasses(key, type, invoker);
    }
}

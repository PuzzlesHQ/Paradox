package com.github.puzzle.paradox.loader.plugin.info;

import com.github.puzzle.paradox.api.records.Version;
import com.github.puzzle.paradox.loader.plugin.AdapterPathPair;
import com.github.puzzle.paradox.loader.plugin.PluginContainer;
import com.github.puzzle.paradox.loader.plugin.PluginJsonInfo;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.hjson.JsonValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.jar.JarFile;

public class PluginInfo {
    public final String DisplayName;
    public final String ID;
    public final Version PlVersion;
    public final String Description;
    public final ImmutableCollection<String> Authors;
    public final ImmutableMap<String, JsonValue> Metadata;
    public final ImmutableMap<String, Version> RequiredDependencies;
    public final ImmutableMap<String, Version> OptionalDependencies;
    public final ImmutableMap<String, ImmutableCollection<AdapterPathPair>> Entrypoints;


    public final PluginJsonInfo JsonInfo;
    private PluginContainer Container;
    public PluginInfo(PluginJsonInfo jsonInfo) {
        JsonInfo = jsonInfo;

        DisplayName = jsonInfo.name();
        ID = jsonInfo.id();
        PlVersion = Version.parseVersion(jsonInfo.version());
        Description = jsonInfo.description();

        Authors = ImmutableList.copyOf(jsonInfo.authors());

        if (jsonInfo.meta() != null) {
            var MetadataBuilder = ImmutableMap.<String, JsonValue>builder();
            for (String key : jsonInfo.meta().keySet()) {
                MetadataBuilder.put(key, jsonInfo.meta().get(key));
            }
            Metadata = MetadataBuilder.build();
        } else Metadata = ImmutableMap.<String, JsonValue>builder().build();

        var EntrypointsBuilder = ImmutableMap.<String, ImmutableCollection<AdapterPathPair>>builder();
        for (String key : jsonInfo.entrypoints().keySet()) {
            EntrypointsBuilder.put(key, ImmutableList.copyOf(jsonInfo.entrypoints().get(key)));
        }
        Entrypoints = EntrypointsBuilder.build();

        if (jsonInfo.dependencies() != null) {
            var RequiredDependenciesBuilder = ImmutableMap.<String, Version>builder();
            for (String key : jsonInfo.dependencies().keySet()) {
                RequiredDependenciesBuilder.put(key, Version.parseVersion(jsonInfo.dependencies().get(key).replaceAll("[^\\d.]", "")));
            }
            RequiredDependencies = RequiredDependenciesBuilder.build();
        } else RequiredDependencies = ImmutableMap.of();

        if (jsonInfo.optional() != null) {
            var OptionalDependenciesBuilder = ImmutableMap.<String, Version>builder();
            for (String key : jsonInfo.optional().keySet()) {
                OptionalDependenciesBuilder.put(key, Version.parseVersion(jsonInfo.optional().get(key).replaceAll("[^\\d.]", "")));
            }
            OptionalDependencies = OptionalDependenciesBuilder.build();
        } else OptionalDependencies = ImmutableMap.of();

    }
    @Contract("_ -> new")
    public static @NotNull PluginInfo fromModJsonInfo(PluginJsonInfo info) {
        return new PluginInfo(info);
    }

    public PluginContainer getOrCreateModContainer() {
        if (Container == null) Container = new PluginContainer(this);
        return Container;
    }

    public PluginContainer getOrCreateModContainer(JarFile file) {
        if (Container != null) Container = new PluginContainer(this, file);
        return Container;
    }

    public static class Builder {

        private String id = null;
        private Version version = null;
        private String name = null;
        private String description = null;
        private List<String> authors = new ArrayList<>();
        private Map<String, Collection<AdapterPathPair>> entrypoints = new HashMap<>();
        private Map<String, JsonValue> meta = new HashMap<>();
        private List<String> mixins = new ArrayList<>();
        private Map<String, Version> dependencies = new HashMap<>();
        private Map<String, Version> optional = new HashMap<>();

        private Builder() {}

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = Version.parseVersion(version);
            return this;
        }

        public Builder setVersion(Version version) {
            this.version = version;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDesc(String desc) {
            this.description = desc;
            return this;
        }

        public Builder setAuthors(String[] authors) {
            this.authors = new ArrayList<>(List.of(authors));
            return this;
        }

        public Builder setAuthors(@NotNull Collection<String> authors) {
            this.authors = authors.stream().toList();
            return this;
        }

        public Builder addAuthors(String... names) {
            this.authors.addAll(List.of(names));
            return this;
        }

        public Builder addAuthor(String name) {
            this.authors.add(name);
            return this;
        }

        public Builder setEntrypoint(String name, Collection<AdapterPathPair> classes) {
            this.entrypoints.put(name, classes);
            return this;
        }

        public Builder addEntrypoint(String name, String adapter, String clazz) {
            if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair(adapter, clazz));
            else {
                List<AdapterPathPair> classes = new ArrayList<>();
                classes.add(new AdapterPathPair(adapter, clazz));
                this.entrypoints.put(name, classes);
            }
            return this;
        }

        public Builder addEntrypoint(String name, String clazz) {
            if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair("java", clazz));
            else {
                List<AdapterPathPair> classes = new ArrayList<>();
                classes.add(new AdapterPathPair("java", clazz));
                this.entrypoints.put(name, classes);
            }
            return this;
        }

        public Builder setEntrypoints(Map<String, Collection<AdapterPathPair>> entrypoints) {
            this.entrypoints = entrypoints;
            return this;
        }

        public Builder setMeta(Map<String, JsonValue> meta) {
            this.meta = meta;
            return this;
        }

        public Builder addMeta(String key, JsonValue value) {
            this.meta.put(key, value);
            return this;
        }

        public Builder setDependencies(Map<String, Version> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public Builder addDependency(String name, Version version) {
            this.dependencies.put(name, version);
            return this;
        }

        public Builder setOptionalDependencies(Map<String, Version> dependencies) {
            this.optional = dependencies;
            return this;
        }

        public Builder addOptionalDependency(String name, Version version) {
            this.optional.put(name, version);
            return this;
        }


        private static @NotNull Map<String, String> TransformDepencenciesMap(@NotNull Map<String, Version> dependencies) {
            Map<String, String> map = new HashMap<>();
            for (String dep : dependencies.keySet()) {
                map.put(dep, dependencies.get(dep).toString());
            }
            return map;
        }

        private String makeId() {
            return id == null ?
                    makeName().replaceAll(" ", "-").toLowerCase(Locale.ROOT) :
                    id;
        }

        private String makeName() {
            return name == null ? "exampleMod" : name;
        }

        public PluginInfo build() {
            return new PluginInfo(new PluginJsonInfo(
                    makeId(),
                    version != null ? version.toString() : "1.0.0",
                    makeName(),
                    description,
                    authors.toArray(new String[0]),
                    entrypoints,
                    meta,
                    mixins.toArray(new String[0]),
                    TransformDepencenciesMap(dependencies),
                    TransformDepencenciesMap(optional)
            ));
        }

        @Contract(" -> new")
        public static @NotNull Builder New() {
            return new Builder();
        }

    }
}

package com.github.puzzle.paradox.loader.providers.api;


import com.github.puzzle.paradox.api.records.Version;

public interface IGameProvider {
    // Game Names
    String getId();
    String getName();

    // Game Version
    Version getGameVersion();
    String getRawVersion();

    // Extra Data
    String getEntrypoint();


    void addBuiltinMods();
}

package com.github.puzzle.paradox.loader.providers.api;


import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.paradox.loader.Version;
import com.github.puzzle.paradox.loader.launch.ParadoxClassLoader;

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

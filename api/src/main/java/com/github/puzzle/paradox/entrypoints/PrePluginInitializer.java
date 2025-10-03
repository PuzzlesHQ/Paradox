package com.github.puzzle.paradox.entrypoints;

public interface PrePluginInitializer {
    String ENTRYPOINT_KEY = "preInit";

    void onPreInit();

}

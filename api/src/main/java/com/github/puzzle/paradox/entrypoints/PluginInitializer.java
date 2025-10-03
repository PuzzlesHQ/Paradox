package com.github.puzzle.paradox.entrypoints;



public interface PluginInitializer {
    String ENTRYPOINT_KEY = "init";

    void onInit();
}

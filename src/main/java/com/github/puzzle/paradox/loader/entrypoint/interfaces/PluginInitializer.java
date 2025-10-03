package com.github.puzzle.paradox.loader.entrypoint.interfaces;



public interface PluginInitializer {
    String ENTRYPOINT_KEY = "init";

    void onInit();
}

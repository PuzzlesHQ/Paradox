package com.github.puzzle.paradox.entrypoints;



public interface PostPluginInitializer {
    String ENTRYPOINT_KEY = "postInit";

    void onPostInit();

}

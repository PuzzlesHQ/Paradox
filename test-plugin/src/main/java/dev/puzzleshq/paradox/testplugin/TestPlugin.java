package dev.puzzleshq.paradox.testplugin;

import com.github.puzzle.paradox.entrypoints.PluginInitializer;

public class TestPlugin implements PluginInitializer {

    @Override
    public void onInit() {
        Constants.LOGGER.info("Hello From INIT");
    }
}

package com.github.puzzle.paradox.loader.lang;

import com.github.puzzle.paradox.loader.lang.impl.JavaLangProvider;
import com.github.puzzle.paradox.loader.plugin.info.PluginInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.ProviderException;
import java.util.HashMap;
import java.util.Map;

public interface ILangProvider {

    Logger LOGGER = LogManager.getLogger("Puzzle | Lang Providers");

    Map<String, ILangProvider> PROVDERS = new HashMap<>();

    ILangProvider JAVA_INSTANCE = new JavaLangProvider();

    static void init() {
        if (!ILangProvider.PROVDERS.containsKey("java")) {
            ILangProvider.PROVDERS.put("java", ILangProvider.JAVA_INSTANCE);
        }
    }

    <T> T create(PluginInfo info, String value, Class<T> type) throws ProviderException;

    static ILangProvider getDefault() {
        return JAVA_INSTANCE;
    }


}
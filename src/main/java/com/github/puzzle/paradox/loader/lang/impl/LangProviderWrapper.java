package com.github.puzzle.paradox.loader.lang.impl;


import com.github.puzzle.paradox.loader.lang.ILangProvider;
import com.github.puzzle.paradox.loader.plugin.info.PluginInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProviderException;

public class LangProviderWrapper implements ILangProvider {

    Object clazz;

    public LangProviderWrapper(Object clazz) {
        LOGGER.info("Wrapping Adapter Class \"" + clazz.getClass().getName() + "\"");
        this.clazz = clazz;
    }

    @Override
    public <T> T create(PluginInfo info, String value, Class<T> type) throws ProviderException {
        Method create = null;
        try {
            create = clazz.getClass().getDeclaredMethod("create", PluginInfo.class, String.class, Class.class);
        } catch (NoSuchMethodException e) {
            throw new ProviderException(e);
        }
        try {
            return (T) create.invoke(clazz, info, value, type);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ProviderException(e);
        }
    }
}
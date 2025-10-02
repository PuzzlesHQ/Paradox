package com.github.puzzle.paradox.loader.launch;

import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.paradox.core.util.Reflection;
import com.github.puzzle.paradox.loader.plugin.PluginLocator;
import com.github.puzzle.paradox.loader.providers.api.IGameProvider;
import com.github.puzzle.paradox.util.MethodUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class PuzzlePiece extends Piece {



    public static void main(String[] args) {
        new PuzzlePiece().launch(args);
    }

    private PuzzlePiece() {
        super(false,null);

        List<URL> classPath = new ArrayList<>();
        try {
            classLoader = (URLClassLoader) Class.forName("dev.puzzleshq.puzzleloader.loader.launch.Piece").getDeclaredField("classLoader").get(null);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
//        blackboard = new HashMap<>();
        try {
            var addURLfunc = Class.forName("dev.puzzleshq.puzzleloader.loader.launch.Piece").getDeclaredMethod("addURL", URL.class);
            addURLfunc.setAccessible(true);
            PluginLocator.crawlPluginFolder(classPath);
            for (URL url : classPath) {
                addURLfunc.invoke(null,url);
            }
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }


}
package com.github.puzzle.paradox.loader.launch;

import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.paradox.core.util.Reflection;
import com.github.puzzle.paradox.loader.plugin.PluginLocator;
import com.github.puzzle.paradox.loader.providers.api.IGameProvider;
import com.github.puzzle.paradox.util.MethodUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.lang.reflect.Method;
import java.net.URL;
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
        classPath.addAll(PluginLocator.getUrlsOnClasspath());
        PluginLocator.crawlPluginFolder(classPath);
        try {
            classLoader = (PuzzleClassLoader) Class.forName("com.github.puzzle.core.loader.launch.Piece").getDeclaredField("classLoader").get(null);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        blackboard = new HashMap<>();
        classPath.forEach(classLoader::addURL);
    }

    private void launch(String[] args) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        final OptionSet options = parser.parse(args);
        try {
            OptionSpec<String> provider_option = parser.accepts("gameProvider").withOptionalArg().ofType(String.class);
            OptionSpec<String> modFolder_option = parser.accepts("pluginFolder").withOptionalArg().ofType(String.class);

            classLoader.addClassLoaderExclusion(DEFAULT_PROVIDER.substring(0, DEFAULT_PROVIDER.lastIndexOf('.')));
            classLoader.addClassLoaderExclusion("com.github.puzzle.paradox.loader.launch");
            classLoader.addClassLoaderExclusion("com.github.puzzle.paradox.loader.entrypoint");
            classLoader.addClassLoaderExclusion("com.github.puzzle.paradox.loader.plugin");
            classLoader.addClassLoaderExclusion("com.github.puzzle.paradox.loader.providers");
            classLoader.addClassLoaderExclusion("com.github.puzzle.paradox.utils");

            if (options.has(provider_option))
                provider = (IGameProvider) Class.forName(provider_option.value(options), true, classLoader).newInstance();
            else
                provider = (IGameProvider) Class.forName(DEFAULT_PROVIDER, true, classLoader).newInstance();



            provider.registerTransformers(classLoader);
            provider.inject(classLoader);
//            Piece.provider.addBuiltinMods();
//            PrePluginInitializer.invokeEntrypoint();
            if (PluginLocator.locatedPlugins == null) PluginLocator.getPlugins();
            Class<?> clazz = Class.forName(provider.getEntrypoint(), false, classLoader);
            Method main = Reflection.getMethod(clazz,"main", String[].class);
            LOGGER.info("Launching {} version {}", provider.getName(), provider.getRawVersion());
//            ServerLauncher.main(args);
            MethodUtil.runStaticMethod(main, (Object) args);
        } catch (Exception e) {
            LOGGER.error("Unable To Launch", e);
            System.exit(1);
        }
    }
}
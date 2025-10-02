package com.github.puzzle.paradox.loader.launch;

import com.github.puzzle.paradox.core.util.Reflection;
import com.github.puzzle.paradox.game.provider.CosmicReachProvider;
import com.github.puzzle.paradox.loader.plugin.PluginLocator;
import com.github.puzzle.paradox.loader.providers.api.IGameProvider;
import com.github.puzzle.paradox.util.MethodUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("deprecation")
public class Piece {
    public String DEFAULT_PROVIDER = CosmicReachProvider.class.getName();
    public static IGameProvider provider;

    public static Map<String, Object> blackboard;
    public static URLClassLoader classLoader;

    public static final Logger LOGGER = LogManager.getLogger("Paradox | Loader");

    public static void main(String[] args) {
        new Piece().launch(args);
    }

    public static void url_main(String[] args, URL[] list){
        new Piece(list).launch(args);
    }
    Piece(){
        this(true,null);
    }
    Piece(URL[] urls){
        this(true,urls);
    }
    Piece(boolean doNormalInit,URL[] urls) {

        if(doNormalInit) {
            List<URL> classPath = new ArrayList<>();
            classPath.addAll(PluginLocator.getUrlsOnClasspath());
            if(urls != null){
                classPath.addAll(List.of(urls));
            }
            classLoader = new ParadoxClassLoader(classPath);


            blackboard = new HashMap<>();

            Thread.currentThread().setContextClassLoader(classLoader);
        }

    }

    public void launch(String[] args) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        final OptionSet options = parser.parse(args);
        try {
                provider = (IGameProvider) Class.forName(DEFAULT_PROVIDER, true, classLoader).newInstance();



//            provider.registerTransformers(classLoader);
//            provider.inject(classLoader);
//            Piece.provider.addBuiltinMods();
//            PrePluginInitializer.invokeEntrypoint();
            List<URL> classPath = new ArrayList<>();
            PluginLocator.crawlPluginFolder(classPath);
            PluginLocator.getPlugins(classPath);
            provider.addBuiltinMods();
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
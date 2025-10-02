package com.github.puzzle.paradox.game.provider;


import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.paradox.api.Paradox;
import com.github.puzzle.paradox.core.PuzzlePL;
import com.github.puzzle.paradox.loader.Version;
import com.github.puzzle.paradox.loader.launch.ParadoxClassLoader;
import com.github.puzzle.paradox.loader.launch.TransformerInitializer;
import com.github.puzzle.paradox.loader.plugin.PluginLocator;
import com.github.puzzle.paradox.loader.plugin.info.PluginInfo;
import com.github.puzzle.paradox.loader.providers.api.IGameProvider;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.server.ServerLauncher;
import org.hjson.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class CosmicReachProvider implements IGameProvider {

    public CosmicReachProvider() {
    }

    @Override
    public String getId() {
        return "cosmic-reach";
    }

    @Override
    public String getName() {
        return "Cosmic Reach";
    }

    @Override
    public Version getGameVersion() {
        try {
            return Version.parseVersion(new String(GameAssetLoader.class.getResourceAsStream("/build_assets/version.txt").readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getRawVersion() {
        try {
            return new String(GameAssetLoader.class.getResourceAsStream("/build_assets/version.txt").readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getEntrypoint() {
        return ServerLauncher.class.getName();
    }


    @Override
    public void addBuiltinMods() {
        /* Puzzle Loader as a Plugin */
        PluginInfo.Builder puzzleLoaderInfo = PluginInfo.Builder.New();
        {
            puzzleLoaderInfo.setName("Puzzle Plugin Loader");
            puzzleLoaderInfo.setDesc("A new dedicated server plugin loader for Cosmic Reach");
            puzzleLoaderInfo.addDependency("cosmic-reach", getGameVersion());

            HashMap<String, JsonValue> meta = new HashMap<>();
            puzzleLoaderInfo.setMeta(meta);
            puzzleLoaderInfo.setAuthors(new String[]{
                    "Mr_Zombii", "Repletsin5", "SinfullySoul","CrabKing"
            });
            puzzleLoaderInfo.setVersion(PuzzlePL.VERSION);
//            puzzleLoaderInfo.addEntrypoint("preInit", PuzzlePL.class.getName());
//            puzzleLoaderInfo.addEntrypoint("init", PuzzlePL.class.getName());
//            puzzleLoaderInfo.addEntrypoint("postInit", PuzzlePL.class.getName());

            PluginLocator.locatedPlugins.put("puzzle-plugin-loader", puzzleLoaderInfo.build().getOrCreateModContainer());
        }
        PluginInfo.Builder apiInfo = PluginInfo.Builder.New();
        {
            apiInfo.setName("Paradox API");
            apiInfo.setDesc("The Paradox plugin API");
            apiInfo.addAuthor("Repletsin5");
            apiInfo.setVersion(PuzzlePL.API_VERSION);
            HashMap<String, JsonValue> meta = new HashMap<>();
            apiInfo.setMeta(meta);
            PluginLocator.locatedPlugins.put("paradox-cr-api", apiInfo.build().getOrCreateModContainer());
        }
        /* Cosmic Reach as a mod */
        PluginInfo.Builder cosmicReachInfo = PluginInfo.Builder.New();
        {
            cosmicReachInfo.setName(getName());
            cosmicReachInfo.setDesc("The base Game");
            cosmicReachInfo.addAuthor("FinalForEach");
            cosmicReachInfo.setVersion(getGameVersion());
            HashMap<String, JsonValue> meta = new HashMap<>();
            cosmicReachInfo.setMeta(meta);
            PluginLocator.locatedPlugins.put(getId(), cosmicReachInfo.build().getOrCreateModContainer());
        }

    }

    static @Nullable File lookForJarVariations(String offs) {
        Pattern type1 = Pattern.compile("Cosmic Reach-\\d+\\.\\d+.\\d+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type2 = Pattern.compile("Cosmic_Reach-\\d+\\.\\d+.\\d+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type3 = Pattern.compile("CosmicReach-\\d+\\.\\d+.\\d+\\.jar", Pattern.CASE_INSENSITIVE);
        for (File f : Objects.requireNonNull(new File(offs).listFiles())) {
            if (type1.matcher(f.getName()).find()) return f;
            if (type2.matcher(f.getName()).find()) return f;
            if (type3.matcher(f.getName()).find()) return f;
            if (f.getName().equals("cosmic_reach.jar")) return f;
            if (f.getName().equals("cosmicreach.jar")) return f;
            if (f.getName().equals("cosmicReach.jar")) return f;
            if (f.getName().equals("Cosmic-Reach.jar")) return f;
        }
        return null;
    }

    static @Nullable File toCrJar(@NotNull File f) {
        if (!f.exists()) return null;
        return f;
    }

    public static String DEFAULT_PACKAGE = "finalforeach.cosmicreach.server";

    static @Nullable File searchForCosmicReach() {
        if (ClassLoader.getPlatformClassLoader().getDefinedPackage(DEFAULT_PACKAGE) == null) {
            File jarFile;
            jarFile = lookForJarVariations(".");
            if (jarFile != null) return toCrJar(jarFile);
            jarFile = lookForJarVariations("../");
            if (jarFile != null) return toCrJar(jarFile);
        }
        return null;
    }
}

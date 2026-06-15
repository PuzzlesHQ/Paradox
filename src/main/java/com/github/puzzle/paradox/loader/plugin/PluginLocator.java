package com.github.puzzle.paradox.loader.plugin;

import com.github.puzzle.paradox.loader.VersionParser;
import com.github.puzzle.paradox.loader.launch.Piece;
import com.github.puzzle.paradox.loader.plugin.info.PluginInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressWarnings("UrlHashCode")
public class PluginLocator {
    public static Logger LOGGER = LogManager.getLogger("Paradox | PluginLocator");

    public static Map<String, PluginContainer> locatedPlugins = new HashMap<>();
    public static final String INFO_FILE_NAME = "paradox.plugin.json";


    @SuppressWarnings("unused")
    public static boolean isPluginLoaded(String pluginId) {
        return locatedPlugins.get(pluginId) != null;
    }

    public static @NotNull Collection<URL> getUrlsOnClasspath() {
        return getUrlsOnClasspath(new ArrayList<>());
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static @NotNull Collection<URL> getUrlsOnClasspath(Collection<URL> urlz) {
        Set<URL> urls = new HashSet<>(urlz);
        // 'urls' may contain URL objects
        // i hope it does

        if (Piece.classLoader != null) {
            Collections.addAll(urls, Piece.classLoader.getURLs());
        } else {
            for (String url : System.getProperty("java.class.path").split(File.pathSeparator)) {
                try {
                    urls.add(new File(url).toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        return urls;
    }

    public static void getPlugins() {
        getPlugins(new ArrayList<>());
    }

    public static void walkDir(File file) {
        if (file.isDirectory()) {
            if (file.listFiles() != null) {
                Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(PluginLocator::walkDir);
            }
        } else if (file.getName().equals(INFO_FILE_NAME)) {
            try {
                String strInfo = new String(new FileInputStream(file).readAllBytes());
                PluginJsonInfo info = PluginJsonInfo.fromString(strInfo);
                LOGGER.info("Discovered Dev Plugin \"{}\" with ID \"{}\"", info.name(), info.id());
                if(locatedPlugins.containsKey(info.id()))
                    throw new RuntimeException("plugin id \""+info.id()+"\" already used");
                else
                    locatedPlugins.put(info.id(), new PluginContainer(PluginInfo.fromModJsonInfo(info), null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void getPlugins(Collection<URL> classPath) {
        Collection<URL> urls = getUrlsOnClasspath(classPath);

        for (URL url : urls) {
            File file = new File(URLDecoder.decode(url.getFile(), Charset.defaultCharset()));
            if (!file.isDirectory()) {
                try {
                    if (file.exists()) {
                        ZipFile jar = new ZipFile(file, ZipFile.OPEN_READ);
                        ZipEntry modJson = jar.getEntry(INFO_FILE_NAME);
                        if (modJson != null) {
                            String strInfo = new String(jar.getInputStream(modJson).readAllBytes());
                            PluginJsonInfo info = PluginJsonInfo.fromString(strInfo);
                            LOGGER.info("Discovered plugin \"{}\" with ID \"{}\"", info.name(), info.id());
                            if(locatedPlugins.containsKey(info.id()))
                                throw new RuntimeException("plugin id \""+info.id()+"\" already used");
                            else
                                locatedPlugins.put(info.id(), new PluginContainer(PluginInfo.fromModJsonInfo(info), jar));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                walkDir(file);
            }
        }

    }

    public static void verifyDependencies() {
        LOGGER.warn("Warning! Only partial semantic versioning support");
        for(var plugin : locatedPlugins.values()){
            if (plugin.INFO.JsonInfo.dependencies() == null) continue;
            if (plugin.INFO.JsonInfo.dependencies().isEmpty()) continue;
            LOGGER.info("Plugin deps for {}", plugin.ID);
            for (Map.Entry<String, String> entry : plugin.INFO.JsonInfo.dependencies().entrySet()) {
                LOGGER.info("\t{}: {}", entry.getKey(), entry.getValue());
                var modDep = locatedPlugins.get(entry.getKey());
                if (modDep == null) {
                    throw new RuntimeException(String.format("can not find mod dependency: %s for plugin id: %s", entry.getKey(), plugin.ID));
                } else {
                    if (!VersionParser.hasDependencyVersion(modDep.VERSION, entry.getValue())) {
                        throw new RuntimeException(String.format("Mod id: %s, requires: %s version of %s, got: %s", plugin.ID, entry.getValue(), modDep.ID, modDep.VERSION));
                    }
                }
            }
        }
    }

    public static void crawlPluginFolder(Collection<URL> urls) {
        File modsFolder = new File("plugins");
        if (!modsFolder.exists()) {
            if (!modsFolder.mkdir()) LOGGER.warn("{} could not be created, provide access to java", modsFolder);
            return;
        }

        for (File modFile : Objects.requireNonNull(modsFolder.listFiles())) {
            try {
                LOGGER.info("Found Jar/Zip {}", modFile);

                urls.add(modFile.toURI().toURL());
            } catch (Exception ignore) {}
        }
    }
}

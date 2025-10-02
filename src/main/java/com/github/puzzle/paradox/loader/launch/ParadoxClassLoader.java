package com.github.puzzle.paradox.loader.launch;

import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import net.minecraft.launchwrapper.IClassNameTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ParadoxClassLoader extends PuzzleClassLoader {
    public static Logger LOGGER = LogManager.getLogger("Paradox | Classloader");

    public ParadoxClassLoader(@NotNull Collection<URL> sources) {
        this(sources.toArray(new URL[0]));
    }

    public ParadoxClassLoader(URL[] sources) {
        super(sources);

        // classloader exclusions
        addClassLoaderExclusion("java.");
        addClassLoaderExclusion("sun.");
        addClassLoaderExclusion("org.apache.logging.");
        addClassLoaderExclusion("com.github.puzzle.paradox.loader.launch.");
        addClassLoaderExclusion("org.slf4j");
        addClassLoaderExclusion("com.google.");
        addClassLoaderExclusion("org.hjson");

        // transformer exclusions
        addTransformerExclusion("javax.");
        addTransformerExclusion("argo.");
        addTransformerExclusion("org.objectweb.asm.");
        addTransformerExclusion("com.google.common.");
        addTransformerExclusion("org.bouncycastle.");
        addClassLoaderExclusion("com.github.puzzle.paradox.loader.");
    }
}

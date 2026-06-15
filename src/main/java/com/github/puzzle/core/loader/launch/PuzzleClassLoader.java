package com.github.puzzle.core.loader.launch;

import com.github.puzzle.paradox.loader.SimpleAssetLoader;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PuzzleClassLoader extends URLClassLoader {
    public static Logger LOGGER = LogManager.getLogger("Puzzle | Classloader");
    public final ClassLoader parent = getClass().getClassLoader();

    protected final List<URL> sources;
    public final Set<String> missingClasses = new HashSet<>();

    public final Set<String> missingResourceCache = new HashSet<>();
    public final Map<String, byte[]> resourceCache = new HashMap<>();

    private final Set<String> classLoaderExceptions = new HashSet<>();

    private IClassNameTransformer renameTransformer;
    public final Map<String, Class<?>> classCache = new HashMap<>();


    public PuzzleClassLoader(URL[] sources, ClassLoader parent) {
        super(sources, parent);
        this.sources = new ArrayList<>(Arrays.asList(sources));

        // classloader exclusions
        addClassLoaderExclusion("java.");
        addClassLoaderExclusion("sun.");
        addClassLoaderExclusion("org.lwjgl.");
        addClassLoaderExclusion("org.apache.logging.");
        addClassLoaderExclusion("org.slf4j");
        addClassLoaderExclusion("com.google.");
        addClassLoaderExclusion("org.hjson");

        addClassLoaderExclusion("com.github.puzzle.access_manipulator.");
        addClassLoaderExclusion("com.github.puzzle.paradox.loader.");
        addClassLoaderExclusion("com.github.puzzle.loader.");
        addClassLoaderExclusion("com.github.puzzle.api.records.");
        addClassLoaderExclusion("com.github.puzzle.api.interfaces.");
        addClassLoaderExclusion("com.github.puzzle.api.enums.");
        addClassLoaderExclusion("com.github.puzzle.api.events.");
        addClassLoaderExclusion("com.github.puzzle.api.commands.");
        addClassLoaderExclusion("com.github.puzzle.entrypoints.");
    }

    private URLConnection getConnection(String name) {
        final URL resource = findResource(name);
        if (resource != null) {
            try {
                return resource.openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public byte[] getResourceBytes(String name) {

        if (missingResourceCache.contains(name)) return null;
        SimpleAssetLoader.RawFileHandle handle = SimpleAssetLoader.getLowLevelClassPathAssetErrors("/".concat(toFileName(name)), false);

        if (handle == null) {
            missingResourceCache.add(name);
            return null;
        }

        byte[] bytes = handle.getBytes();
        resourceCache.put(name, bytes);
        handle.dispose();
        return bytes;
    }
    private static Method I_HATE_YOU_JAVA_CLASSLOADERS;
    private static boolean aboveJava8;

    static {
        try {
            I_HATE_YOU_JAVA_CLASSLOADERS = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            I_HATE_YOU_JAVA_CLASSLOADERS.setAccessible(true);
        } catch (Exception e) {
            aboveJava8 = true;
        }
    }

    @Override
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        if (missingClasses.contains(name)) throw new ClassNotFoundException(name);

        if (name.endsWith(".package-info")) return parent.loadClass(name);

        for (String exclusion : classLoaderExceptions) {
            if (name.startsWith(exclusion)) return parent.loadClass(name);
        }

        if (aboveJava8){

        } else {
            Class<?> parentTest;
            try {
                parentTest = (Class<?>) I_HATE_YOU_JAVA_CLASSLOADERS.invoke(parent, name);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if (parentTest != null) return parentTest;
        }

        if (isClassLoaded(name))
            return classCache.get(name);

        try {
            int lastDot = name.lastIndexOf('.');
            String pkgName = lastDot == -1 ? "" : name.substring(0, lastDot);

            String classFileNameSoft = name.replaceAll("\\.", "/") + ".class";
            String fileName = toFileName(name);

            URLConnection connection = getConnection(classFileNameSoft);
            CodeSigner[] signers = null;

            CodeSource source = null;

            if (connection instanceof JarURLConnection) {
                JarURLConnection  jarConnection = (JarURLConnection) connection;

                JarFile jar = jarConnection.getJarFile();

                if (jar != null && jar.getManifest() != null) {
                    JarEntry entry = jar.getJarEntry(fileName);

                    getResourceBytes(name);
                    signers = entry.getCodeSigners();
                    source = new CodeSource(jarConnection.getJarFileURL(), signers);
                }
                if (lastDot > -1) {
                    if (getPackage(pkgName) == null)
                        definePackage(pkgName, jarConnection.getManifest(), jarConnection.getJarFileURL());
                }
            } else {
                if (lastDot > -1) {
                    Package pkg = getPackage(pkgName);
                    if (pkg == null)
                        definePackage(pkgName, null, null, null, null, null, null, null);
                }
            }

            if (connection == null) {
                try {
                    return parent.loadClass(name);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            byte[] bytes = getResourceBytes(name);
            if (bytes == null) {
                missingClasses.add(name);
                throw new ClassNotFoundException(name);
            }
            Class<?> clazz;
            try {
                clazz = defineClass(name, bytes, 0, bytes.length, source);
                classCache.put(name, clazz);
            } catch (LinkageError e) {
                if (e.getMessage().contains("previously loaded")) {
                    return Class.forName(name, false, parent);
                }
                throw e;
            }
            return clazz;
        } catch (IOException e) {
            missingClasses.add(name);
            throw new ClassNotFoundException(name, e);
        }
    }
    public boolean isClassLoaded(String name) {
        return this.classCache.containsKey(name);
    }
    @Override
    public void addURL(final URL url) {
        super.addURL(url);
        sources.add(url);
    }
    private String toFileName(String name) {
        return name.replace('.', '/').concat(".class");
    }
    public void addClassLoaderExclusion(String toExclude) {
        classLoaderExceptions.add(toExclude);
    }

}
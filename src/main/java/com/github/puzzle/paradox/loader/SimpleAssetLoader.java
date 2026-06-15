package com.github.puzzle.paradox.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Loads raw assets.(simpler version of RawAssetLoader from puzzle-core)
 *
 * @author replet, Mr_Zombii
 */
public class SimpleAssetLoader {

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            while ((readLen = inputStream.read(buf, 0, bufLen)) != -1) {
                outputStream.write(buf, 0, readLen);
            }
            return outputStream.toByteArray();
        }
    }

    private static final Logger LOGGER = LogManager.getLogger("Paradox | SimpleAssetLoader");

    /**
     * Gets the bytes from a {@link InputStream}.
     * @param stream the inputStream to get the bytes from.
     */
    private static byte[] getBytesFromStream(InputStream stream) {
        try {
            byte[] bytes = readAllBytes(stream);
            stream.close();
            return bytes;
        } catch (Exception ignore) {
            return null;
        }
    }

    public static RawFileHandle getLowLevelZipAssetErrors(ZipFile file, String path, boolean errorOut) {
        try {
            ZipEntry entry = file.getEntry(path);
            InputStream stream = file.getInputStream(entry);
            String[] strings = entry.getName().split("/");
            return new RawFileHandle(getBytesFromStream(stream), strings[strings.length - 1]);
        } catch (Exception ignore) {
            if (errorOut) LOGGER.error("Cannot find resoxurce {} in zip {}.", path, file);
            return null;
        }
    }

    /**
     * Gets the asset from a zip file.
     * @param file the zip file to get the file from.
     * @param path the path of the file.
     * @return a {@link RawFileHandle}
     */
    public static RawFileHandle getLowLevelZipAsset(ZipFile file, String path) {
        return getLowLevelZipAssetErrors(file, path, true);
    }


    public static URL getLowLevelClassPathUrl(String path) {
        String softPath;

        if (path.startsWith("/")) {
            softPath = ("/" + path).replaceAll("//", "");
        } else {
            softPath = path;
        }

        return Thread.currentThread().getContextClassLoader().getResource(softPath);
    }

    /**
     * Gets the asset from the class path.
     * @param path the path of the asset.
     * @return a {@link RawFileHandle}
     */
    public static RawFileHandle getLowLevelClassPathAssetErrors(String path, boolean errorOut) {
        String softPath;

        if (path.startsWith("/")) {
            softPath = ("/" + path).replaceAll("//", "");
        } else {
            softPath = path;
        }

        URL url = Thread.currentThread().getContextClassLoader().getResource(softPath);

        try {
            return new RawFileHandle(getBytesFromStream(url.openStream()), url.getFile());
        } catch (Exception ignore) {
            if (errorOut) LOGGER.error("Cannot find resource {} on the classpath.", path);
            return null;
        }
    }

    public static RawFileHandle getLowLevelClassPathAsset(String path) {
        return getLowLevelClassPathAssetErrors(path, true);
    }


    /**
     * Gets the asset relative to the directory.
     * @param dir the directory to start at.
     * @param path the asset path.
     * @return a {@link RawFileHandle}
     */
    public static RawFileHandle getLowLevelRelativeAssetErrors(File dir, String path, boolean errors) {
        try {
            URL url = new File(dir, path).toURI().toURL();
            InputStream stream = url.openStream();
            return new RawFileHandle(getBytesFromStream(stream), url.getFile());
        } catch (IOException ignore) {
            if (errors) LOGGER.error("Cannot find resource relative {} in dir {}", path, dir);
            return null;
        }
    }

    public static RawFileHandle getLowLevelRelativeAsset(File dir, String path) {
        return getLowLevelRelativeAssetErrors(dir, path, true);
    }



    public static class RawFileHandle {

        byte[] bytes;
        String file;

        public RawFileHandle(byte[] bytes, String file) {
            this.bytes = bytes;
            this.file = file;
        }

        /**
         * Gets the file of the RawFileHandle.
         */
        public String getFile() {
            return file;
        }

        /**
         * Gets the bytes of the RawFileHandle.
         */
        public byte[] getBytes() {
            return bytes;
        }

        /**
         * Gets the RawFileHandle as a string.
         */
        public String getString() {
            return new String(getBytes());
        }

        /**
         * Dispose of the RawFileHandle.
         */
        public void dispose() {
            bytes = null;
            file = null;
        }

        @Override
        public String toString() {
            return "{ RawFileHandle, Name: \"" + file + "\", Size: \"" + bytes.length + "\" }";
        }
    }

}
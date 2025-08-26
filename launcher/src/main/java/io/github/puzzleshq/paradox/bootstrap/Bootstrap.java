package io.github.puzzleshq.paradox.bootstrap;

import io.sigpipe.jbsdiff.InvalidHeaderException;
import io.sigpipe.jbsdiff.Patch;
import org.apache.commons.compress.compressors.CompressorException;
import org.hjson.HjsonOptions;
import org.hjson.JsonObject;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.invoke.MethodHandles.*;


public class Bootstrap {
    public static void patch(File oldFile, File newFile, InputStream patchFile)
            throws CompressorException, FileNotFoundException, InvalidHeaderException,
            IOException {
        FileInputStream oldIn = new FileInputStream(oldFile);
        byte[] oldBytes = new byte[(int) oldFile.length()];
        oldIn.read(oldBytes);
        oldIn.close();

        var patchBytes = patchFile.readAllBytes();
        patchFile.close();

        FileOutputStream out = new FileOutputStream(newFile);
        if(!newFile.exists())
            Files.createFile(newFile.toPath());
        Patch.patch(oldBytes, patchBytes, out);
        out.close();
    }

    static final String CR_DOWNLOAD_URL = "https://github.com/PuzzlesHQ/CRArchive/releases/download/%s/cosmic-reach-server-%s.jar";
    static final String MAIN_CLASS = "com.github.puzzle.paradox.loader.launch.Piece";
    public static void downloadFile(URL url, Path toPlace ){
        InputStream in = null;
        try {
            in = url.openStream();
            System.out.println("Downloading : " + url);
            Files.copy(in, toPlace, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = Bootstrap.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("dependencies.json");
//        String data = readFromInputStream(new BufferedReader(new InputStreamReader(inputStream)),);
        assert inputStream != null;
        var obj = JsonObject.readHjson(new BufferedReader(new InputStreamReader(inputStream)),new HjsonOptions());

//        System.out.println(obj.toString(Stringify.FORMATTED));
        var depsObj = obj.asObject().get("dependencies").asArray();
        List<String> deps = new ArrayList<>();
        System.out.println("Checking dependencies...");
        for (var _dep : depsObj) {
            var dep = _dep.asObject();
            String name = dep.get("name").asString();
            var parsed = name.split(":");
            if(parsed.length > 3){
                throw new RuntimeException("Dep name invalid");
            }
            String filename = parsed[1] + "-" + parsed[2] + ".jar";

            var folderStructure = parsed[0].split("\\.");

            StringBuilder sb = new StringBuilder();
//            sb.append(Paths.get("").toAbsolutePath()).append("/").append("deps").append("/");
            for (String s : folderStructure) {
                sb.append(s);
                sb.append("/");
            }
            sb.append(parsed[1]).append("/").append(parsed[2]);
            sb.append("/").append(filename);
            Path toDownload =  Path.of("","libraries",sb.toString()).toAbsolutePath();
            deps.add(toDownload.toString());
            if(Files.exists(toDownload)){
                int size = dep.get("size").asInt();
                if(toDownload.toFile().length()!= size){
                    try {
                        downloadFile(new URL(dep.get("url").asString()),toDownload);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    continue;
                }
            }
            try {
                Files.createDirectories(toDownload.getParent());
                downloadFile(new URL(dep.get("url").asString()),toDownload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Path server = Path.of("", "server","Paradox.jar");
        if(!server.toFile().exists() || server.toFile().isDirectory()){
            System.out.println("Patching server...");
            try {
               Files.delete(server);
            }catch (NoSuchFileException ignored){

            }
            Files.createDirectories(server.getParent());
            String crVersion = obj.asObject().get("cr-version").asString();
            Path tempFolder = Path.of("", "temp");
            Files.createDirectory(tempFolder);
            Path temp = Path.of("", "temp", "CosmicReach-Server.jar");
            downloadFile(new URL(CR_DOWNLOAD_URL.formatted(crVersion,crVersion)), temp);
            var patchURI =  classLoader.getResourceAsStream("jarpatch.patch");
            try {
                assert patchURI != null;
                patch(temp.toAbsolutePath().toFile(), server.toAbsolutePath().toFile(),patchURI);
                Files.deleteIfExists(temp);
                Files.delete(tempFolder);
            } catch (CompressorException | InvalidHeaderException e) {
                throw new RuntimeException(e);
            }
        }

//        System.out.println("Creating run scripts...");
        //TODO:

        System.out.println("Launching server...");
        List<URL> classPath = new ArrayList<>();
        for (String dep : deps) {
//            System.out.println(dep);
            classPath.add(new URL("file:///" + dep));
        }
        classPath.add(server.toUri().toURL());
        final ClassLoader parentClassLoader = Bootstrap.class.getClassLoader().getParent();
        final URLClassLoader newClassLoader = new URLClassLoader(classPath.toArray(new URL[0]), parentClassLoader);
        final Thread runThread = new Thread(() -> {
            try {
                final Class<?> mainClass = Class.forName(MAIN_CLASS, true, newClassLoader);
                final MethodHandle mainHandle = lookup().findStatic(mainClass, "url_main", MethodType.methodType(void.class, String[].class,URL[].class)).asFixedArity();
                mainHandle.invoke((Object) args,(Object) classPath.toArray(new URL[0]));
            } catch (final Throwable t) {
                throw new RuntimeException(t);
            }
        }, "Server");
        runThread.setContextClassLoader(newClassLoader);
        runThread.start();
    }

}

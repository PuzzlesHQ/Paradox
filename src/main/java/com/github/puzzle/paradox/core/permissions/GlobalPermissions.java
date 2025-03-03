package com.github.puzzle.paradox.core.permissions;

import finalforeach.cosmicreach.savelib.IByteArray;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static finalforeach.cosmicreach.io.SaveLocation.getSaveFolderLocation;

public class GlobalPermissions {
    public static PermissionGroup DEFAULT_GROUP;
    private static final Map<String,PermissionGroup> loadedGroups = new HashMap<>();
    static public Permission getPermission(String permissionName){
        return allPermissions.get(permissionName);
    }
    static public Set<String> getPermissionGroupStringSet(){
        return loadedGroups.keySet();
    }

    public static Map<String,Permission> allPermissions = new HashMap<>();

    static public void addPermission(Permission permission){
        assert(permission != null);
        allPermissions.putIfAbsent(permission.getPermissionVarString(), permission);
    }

    private static final Map<String,PlayerPermissions> loadedPlayerPerms = new HashMap<>();

    ////
    //Here for API if internal changes occur
    public static PlayerPermissions getPlayerPermissions(@NotNull String playerID){
        return loadedPlayerPerms.get(playerID);
    }
    public static void setPlayerPermissions(@NotNull String playerID,@NotNull PlayerPermissions permissions){
        loadedPlayerPerms.put(playerID,permissions);
    }

    public static void addPermissionGroup(@NotNull String name, @NotNull String... perms){
        loadedGroups.put(name,new PermissionGroup(name,perms));
    }
    public static void addPermissionGroup(@NotNull String name, @NotNull PermissionGroup p){
        loadedGroups.put(name,p);
    }
    public static void removePermissionGroup(@NotNull String name){
        loadedGroups.remove(name);
    }
    public static PermissionGroup getPermissionGroup(@NotNull String name){
       return loadedGroups.get(name);
    }

    public static void loadPlayerPermissions(){
        var fileLoc = getSaveFolderLocation() + "/permissions/"  + "playerperms.crbin";
        CRBinDeserializer crBinDeserializer = CRBinDeserializer.getNew();
        try (FileInputStream stream = new FileInputStream(fileLoc)) {
          var data = stream.readAllBytes();
          crBinDeserializer.prepareForRead(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var perms = crBinDeserializer.readObjArray("playerperms",PlayerPermissions.class);
        for (var p : perms){
            loadedPlayerPerms.put(p.playerID,p);
        }
    }
    public static void savePlayerPermissions(){
        PlayerPermissions[] array = new PlayerPermissions[loadedPlayerPerms.size()];
        ArrayList<PlayerPermissions> arrayList = new ArrayList<>(loadedPlayerPerms.size());
        arrayList.addAll(loadedPlayerPerms.values());
        arrayList.toArray(array);
        CRBinSerializer crbinserializer = CRBinSerializer.getNew();
        crbinserializer.writeObjArray("playerperms",array,0,arrayList.size());
        var fileLoc = getSaveFolderLocation() + "/permissions/"  + "playerperms.crbin";
        IByteArray data = crbinserializer.toByteArray();
        var file = new File(fileLoc);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream fos = new FileOutputStream(file,false)) {
            fos.write(data.items(),0, data.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePermissionGroups(){
        PermissionGroup[] array = new PermissionGroup[loadedGroups.size()];
        ArrayList<PermissionGroup> arrayList = new ArrayList<>(loadedGroups.size());
        arrayList.addAll(loadedGroups.values());
        arrayList.toArray(array);
        CRBinSerializer crbinserializer = CRBinSerializer.getNew();
        crbinserializer.writeObjArray("permgroups",array,0,arrayList.size());
        var fileLoc = getSaveFolderLocation() + "/permissions/"  + "permissiongroups.crbin";
        IByteArray data = crbinserializer.toByteArray();
        var file = new File(fileLoc);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream fos = new FileOutputStream(file,false)) {
            fos.write(data.items(),0, data.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadPermissionGroups(){
        var fileLoc = getSaveFolderLocation() + "/permissions/"  + "permissiongroups.crbin";
        CRBinDeserializer crBinDeserializer = CRBinDeserializer.getNew();
        try (FileInputStream fis = new FileInputStream(fileLoc)) {
            var data = fis.readAllBytes();
            crBinDeserializer.prepareForRead(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var perms = crBinDeserializer.readObjArray("permgroups",PermissionGroup.class);
        for (var p : perms){
            loadedGroups.put(p.getName(),p);
        }
    }
}

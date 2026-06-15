package com.github.puzzle.paradox.core.permissions;

import com.github.puzzle.paradox.core.PuzzlePL;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;
import finalforeach.cosmicreach.savelib.crbin.ICRBinSerializable;

import java.util.HashMap;

public class PermissionGroup implements ICRBinSerializable {
    public PermissionGroup(String name, String... perms){
        this.permissionGroupName = name;
        for(String permString : perms){
            assert permString == null || permString.isBlank();
            if(GlobalPermissions.getPermission(permString) != null)
                permissionList.putIfAbsent(permString,GlobalPermissions.getPermission(permString));
        }
    }
    // DO NOT USE, ONLY FOR ICRBinSerializable TO WORK
    public PermissionGroup(){

    }
    String permissionGroupName;
    public String getName(){
        return permissionGroupName;
    }

    HashMap<String,Permission> permissionList = new HashMap<>();

    public void add(Permission perm){
        if(perm == null)
            throw new RuntimeException("Permission cannot be null");
        permissionList.putIfAbsent(perm.permissionVarString, perm);

    }
    public void remove(String permissionVarString){
        permissionList.remove(permissionVarString);
    }

    public boolean contains(Permission permission){
        return contains(permission.permissionVarString);
    }
    public boolean contains(String permissionVarString){
        return permissionList.get(permissionVarString) != null;
    }

    @Override
    public void read(CRBinDeserializer deserializer) {
        permissionGroupName = deserializer.readString("name");
        var permissionListStrings = deserializer.readStringArray("permissionListStrings");
        for (var s : permissionListStrings){
            permissionList.putIfAbsent(s, GlobalPermissions.getPermission(s));
        }
    }


    @Override
    public void write(CRBinSerializer serializer) {
        serializer.writeString("name",permissionGroupName);
        String[] array = new String[permissionList.size()];
        var ks = permissionList.keySet().toArray(array);
        serializer.writeStringArray("permissionListStrings",ks,permissionList.size());

    }
}

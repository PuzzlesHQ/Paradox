package com.github.puzzle.paradox.core.permissions;

public class Permission {
    public Permission(String varString, int value) {
        assert(varString == null || varString.isBlank());
        this.permissionVarString = varString;
        this.value = value;
    }

    public String getPermissionVarString() {
        return permissionVarString;
    }

    String permissionVarString = "";

    int value;
}

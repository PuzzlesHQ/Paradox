package com.github.puzzle.paradox.game.command;

import com.github.puzzle.paradox.api.commands.CommandSource;
import com.github.puzzle.paradox.api.impl.player.ParadoxAccount;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxAccount;
import com.github.puzzle.paradox.core.permissions.GlobalPermissions;
import com.github.puzzle.paradox.core.permissions.Permission;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;

public abstract class ParadoxBrigaderCommand implements Command<CommandSource> {


    public abstract String getName();
    public abstract String[] getAliases();
    protected Permission permission;

    abstract int execute(CommandContext<CommandSource> context) throws CommandSyntaxException;
    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        IParadoxAccount paradoxAccount =  context.getSource().getAccount();
        Account account = null;
        if(paradoxAccount != null) {
            account = ((ParadoxAccount)paradoxAccount).getInternalAccount();
        }
        var perm = this.getPermission().getPermissionVarString();
        if(account != null) {
            if (!GlobalPermissions.getPlayerPermissions(account.getUniqueId()).hasPermission(perm) && !account.isOperator()) {
                var packet = new MessagePacket("You do not have permission to use this command");
                packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                packet.setupAndSend(
                        ServerSingletons
                                .getIdentityByAccount(account));
                return 0;
            }
        }
        return execute(context);
    }
    public void registerPermission(String basePermissionsName, int permissionValue){
        registerPermission( basePermissionsName, permissionValue, false);
    }
    public void registerPermission(String basePermissionsName, int permissionValue, boolean isConsoleCommand){
        permission = new Permission(basePermissionsName + "." + getName(),permissionValue);
        GlobalPermissions.addPermission(permission);
    }

    public Permission getPermission() {
        return permission;
    }

}

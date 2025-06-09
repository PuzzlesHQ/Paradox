package com.github.puzzle.paradox.game.command.chat;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.core.permissions.GlobalPermissions;
import com.github.puzzle.paradox.core.permissions.PermissionGroup;
import com.github.puzzle.paradox.game.command.DefaultPuzzleCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import org.slf4j.LoggerFactory;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;
import static com.github.puzzle.paradox.core.PuzzlePL.sendbackChat;

public class Perms extends DefaultPuzzleCommand {
    @Override
    public String getName() {
        return "perms";
    }
    public Perms() {
        registerPermission(100_000_000);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    //TODO: Improve code readability
    @Override
    public int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        var account = context.getSource().getAccount();
        var args = StringArgumentType.getString(context, "args");
        var split = args.split(" ");
        if(split.length > 5) {
            sendbackChat("Incorrect args", account);
            return 0;
        }
        if(split.length < 3){
            if ( split.length == 1 && split[0].equals("save")) {
                GlobalPermissions.savePlayerPermissions();
                GlobalPermissions.savePermissionGroups();
                sendbackChat("Saved permissions", account);
            }else if(split.length==2){
                if (split[0].equals("list")){
                    if(split[1].equals("group")){
                        StringBuilder sb = new StringBuilder();
                        for (var p : GlobalPermissions.getPermissionGroupStringSet()){
                            sb.append(" " + p + "\n");
                        }
                        sendbackChat("Groups:\n" + sb,account);
                    }else if(split[1].equals("perms")){
                        StringBuilder sb = new StringBuilder();
                        for (var p : GlobalPermissions.allPermissions.keySet()){
                            sb.append(" " + p + "\t");
                        }
                        sendbackChat("Permissions:\n" + sb,account);
                    }
                }
            }

            else {
                sendbackChat("Incorrect args", account);
            }
            return 0;

        }
        if(split[0].equals("group")){
            var group = GlobalPermissions.getPermissionGroup(split[1]);
            if(split[2].equals("add")){
                if(split.length != 4){
                    sendbackChat("Incorrect args", account);
                    return 0;
                }
                if(group == null){
                    sendbackChat("Could not find group '" + split[1] + "'",account);
                    return 0;
                }
                var p = GlobalPermissions.getPermission(split[3]);
                if( p == null){
                    sendbackChat("Could not find permission '" + split[3] + "'",account);
                    return 0;
                }
                if(group.contains(p)){
                    sendbackChat("Group '"+ group.getName() +"' already has permission '" + p.getPermissionVarString() + "'",account);
                    return 0;
                }
                group.add(p);
                sendbackChat("Added permission",account);
                return 0;
            }
            if(split[2].equals("create")){
                if(group != null){
                    sendbackChat("Group already exists'" + split[1] + "'",account);
                    return 0;
                }
                group = new PermissionGroup(split[1]);
                GlobalPermissions.addPermissionGroup(split[1],group);
                sendbackChat("Group created",account);
                return 0;
            }
            else if(split[2].equals("delete")){
                if(group == null){
                    sendbackChat("Group '" + split[1] + "' not found",account);
                    return 0;
                }
                if(group.getName().equals("default")){
                    sendbackChat("Group '" + split[1] + "' can not be deleted",account);
                    return 0;
                }

                group = new PermissionGroup(split[1]);
                GlobalPermissions.addPermissionGroup(split[1],group);
                sendbackChat("Group deleted",account);
                return 0;
            }
            sendbackChat("Incorrect args", account);
            return 0;
        }
        else if (split[0].equals("player")){
            var player = ServerSingletons.getAccountByUniqueId(split[1]);
            if(player == null){
                sendbackChat("Could not find player '" + split[1] + "'",account);
                return 0;
            }
            if(split[2].equals("add")){
                if(split[3].equals("group")){
                    if(split.length != 4){
                        sendbackChat("Incorrect args", account);
                        return 0;
                    }
                    var group = GlobalPermissions.getPermissionGroup(split[4]);
                    if(group == null){
                        sendbackChat("Could not find group '" + split[4] + "'",account);
                        return 0;
                    }
                    var curPerm = GlobalPermissions.getPlayerPermissions(player.getUniqueId());
                    curPerm.addGroup(group);
                    GlobalPermissions.setPlayerPermissions(player.getUniqueId(),curPerm);
                    sendbackChat("Added group",account);
                    return 0;
                }
                else if(split[3].equals("perm")){
                    if(split.length != 4){
                        sendbackChat("Incorrect args", account);
                        return 0;
                    }
                    var p = GlobalPermissions.getPermission(split[4]);
                    if( p == null){
                        sendbackChat("Could not find permission '" + split[4] + "'",account);
                        return 0;
                    }
                    var curPerm = GlobalPermissions.getPlayerPermissions(player.getUniqueId());
                    curPerm.addIndividualPermission(p);
                    GlobalPermissions.setPlayerPermissions(player.getUniqueId(),curPerm);
                    sendbackChat("Added permission",account);
                    return 0;
                }
                sendbackChat("Incorrect args", account);
                return 0;

            } else if (split[2].equals("remove")) {
                if(split.length != 4){
                    sendbackChat("Incorrect args", account);
                    return 0;
                }
                if(split[3].equals("group")){
                    var group = GlobalPermissions.getPermissionGroup(split[4]);
                    if(group == null){
                        sendbackChat("Could not find group '" + split[4] + "'",account);
                        return 0;
                    }
                    var curPerm = GlobalPermissions.getPlayerPermissions(player.getUniqueId());
                    curPerm.removeGroup(group);
                    GlobalPermissions.setPlayerPermissions(player.getUniqueId(),curPerm);
                    return 0;
                }
                else if(split[3].equals("perm")){
                    var p = GlobalPermissions.getPermission(split[4]);
                    if( p == null){
                        sendbackChat("Could not find permission '" + split[4] + "'",account);
                        return 0;
                    }
                    var curPerm = GlobalPermissions.getPlayerPermissions(player.getUniqueId());
                    curPerm.removeIndividualPermission(p);
                    GlobalPermissions.setPlayerPermissions(player.getUniqueId(),curPerm);
                    return 0;
                }
                sendbackChat("Incorrect args", account);
                return 0;
            }
        }

        /*TODO:
            this command layout example

            perm group <group> add <perm>
            perm group <group> create
            perm group <group> remove <perm>
            perm group <group> delete
            perm player <player> add perm <perm>
            perm player <player> add group <group>
            perm player <player> remove perm <perm>
            perm player <player> remove group <group>
            perm list group
            perm list perms

            dont allow deleting of 'default' group
         */
        sendbackChat("Incorrect args", account);
        return 0;
    }
}

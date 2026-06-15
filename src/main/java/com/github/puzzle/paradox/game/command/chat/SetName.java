package com.github.puzzle.paradox.game.command.chat;

import com.github.puzzle.paradox.api.commands.CommandSource;
import com.github.puzzle.paradox.api.impl.player.ParadoxAccount;
import com.github.puzzle.paradox.game.command.DefaultPuzzleCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;

public class SetName extends DefaultPuzzleCommand {

    public SetName() {
        registerPermission(0);
    }

    @Override
    public int execute(CommandContext<CommandSource> context) {
        String name =  StringArgumentType.getString(context, "name");
        if(name.length() > 25) {

            var packet = new MessagePacket("Name can be a max of 25 chars");
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(((ParadoxAccount)context.getSource().getAccount()).getInternalAccount()));
            return 0;
        }
        boolean isTaken = false;
        for (var id : ServerSingletons.SERVER.authenticatedConnections){
            if(ServerSingletons.SERVER.getAccount(id.ctx).displayname.equals(name)) {
                isTaken = true;
                break;
            }
        }
        if(isTaken){
            var packet = new MessagePacket("Name is already used");
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(((ParadoxAccount)context.getSource().getAccount()).getInternalAccount()));
            return 0;
        }
        context.getSource().getAccount().setUsername(name);
        var packet = new MessagePacket("Set your name to: " + name);
        packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
        packet.setupAndSend(
                ServerSingletons
                        .getIdentityByAccount(((ParadoxAccount)context.getSource().getAccount()).getInternalAccount()));
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}

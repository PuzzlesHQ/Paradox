package com.github.puzzle.paradox.game.command.chat;

import com.github.puzzle.paradox.api.commands.CommandSource;
import com.github.puzzle.paradox.api.impl.player.ParadoxAccount;
import com.github.puzzle.paradox.game.command.DefaultPuzzleCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;

public class Msg extends DefaultPuzzleCommand {
    public Msg() {
        registerPermission(0);
    }

    @Override
    public int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String name =  StringArgumentType.getString(context, "name");
        String message =  StringArgumentType.getString(context, "msg");
        if(name.length() > 25) {

            var packet = new MessagePacket("Name can only be a max of 25 chars");
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(((ParadoxAccount)context.getSource().getAccount()).getInternalAccount()));
            return 0;
        }
        if(message.length() > MessagePacket.MAX_MESSAGE_LENGTH){
            var packet = new MessagePacket("Message can only be a max of "+MessagePacket.MAX_MESSAGE_LENGTH+" chars");
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(((ParadoxAccount)context.getSource().getAccount()).getInternalAccount()));
            return 0;
        }

        for (var id : ServerSingletons.SERVER.authenticatedConnections){
            var acc = ServerSingletons.SERVER.getAccount(id.ctx);
            if(acc.displayname.equals(name)) {
                var packet = new MessagePacket("["
                        + context.getSource().getAccount().getDisplayName()
                        + " -> " + acc.displayname + "] " +
                        message);
                packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                packet.setupAndSend(id);
                packet = new MessagePacket("Sent message to: " + name);
                packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                packet.setupAndSend(
                        ServerSingletons
                                .getIdentityByAccount(((ParadoxAccount)context.getSource().getAccount()).getInternalAccount()));

                return 0;
            }
        }
        var packet = new MessagePacket("Can't find player: " + name);
        packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
        packet.setupAndSend(
                ServerSingletons
                        .getIdentityByAccount(((ParadoxAccount)context.getSource().getAccount()).getInternalAccount()));
        return 0;
    }

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}

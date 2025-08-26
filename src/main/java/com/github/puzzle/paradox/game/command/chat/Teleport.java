package com.github.puzzle.paradox.game.command.chat;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.game.command.DefaultPuzzleCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.packets.entities.PlayerPositionPacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.singletons.GameSingletons;

import java.util.Objects;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;

public class Teleport {

    public static class TPR extends DefaultPuzzleCommand {

        public TPR() {
            registerPermission(0);
        }

        @Override
        public int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
            String name = StringArgumentType.getString(context, "name");
            if(name.length() > 25)
            {
                var packet = new MessagePacket("Player name cannot be longer than 25 chars");
                packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                packet.setupAndSend(
                        ServerSingletons
                                .getIdentityByAccount(context.getSource().getAccount()));
                return 0;
            }
            for(var id : ServerSingletons.SERVER.authenticatedConnections){
                if (Objects.equals(ServerSingletons.getAccount(id).getDisplayName(), name)){
                    Player playerToTp = context.getSource().getAccount().getPlayer();

                    ServerSingletons.getAccount(id).addTpr(id,playerToTp);

                    var packet = new MessagePacket(context.getSource().getAccount().displayname + " Request to tp");
                    packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                    packet.setupAndSend(id);

                    packet = new MessagePacket( "Use command .tpa to accept");
                    packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                    packet.setupAndSend(id);

                    packet = new MessagePacket("Request tp to: " + name);
                    packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                    packet.setupAndSend(
                            ServerSingletons
                                    .getIdentityByAccount(context.getSource().getAccount()));
                    return 0;
                }
            }
            var packet = new MessagePacket("Cannot find player: " + name);
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(context.getSource().getAccount()));
            return 0;
        }

        @Override
        public String getName() {
            return "tpr";
        }

        @Override
        public String[] getAliases() {
            return new String[0];
        }
    }

    public static class TPA extends DefaultPuzzleCommand {
        public TPA() {
            registerPermission(0);
        }

        @Override
        public int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
            context.getSource().getAccount();
            if (context.getSource().getAccount().tpRequst){
                Player player = context.getSource().getAccount().getTprPlayer();
                Player playerToTp = context.getSource().getAccount().getTprToPlayer();

                Vector3 vector3 = player.getPosition();

                playerToTp.setPosition(vector3.x, vector3.y, vector3.z);
                if (finalforeach.cosmicreach.singletons.GameSingletons.isHost && ServerSingletons.SERVER != null) {
                    ServerSingletons.SERVER.broadcastToAll(new PlayerPositionPacket(playerToTp));
                }
                return 0;
            }
            var packet = new MessagePacket("You have no tpa requests");
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(context.getSource().getAccount()));
            return 0;
        }

        @Override
        public String getName() {
            return "tpa";
        }

        @Override
        public String[] getAliases() {
            return new String[0];
        }
    }
}

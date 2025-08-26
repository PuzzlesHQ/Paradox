package com.github.puzzle.paradox.game.command;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.game.commands.ParadoxCommandSource;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.netty.channel.ChannelHandlerContext;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;
import static com.github.puzzle.paradox.core.PuzzlePL.clientDispatcher;
import static finalforeach.cosmicreach.singletons.GameSingletons.world;
import static finalforeach.cosmicreach.networking.server.ServerSingletons.OP_LIST;

public class CommandParsing {
    public static void parse(GamePacket packet, String message, NetworkIdentity identity, ChannelHandlerContext ctx){
        try {

            ParseResults<CommandSource> results = clientDispatcher.parse(message.substring(1),new ParadoxCommandSource(ServerSingletons.getAccount(identity),ServerSingletons.SERVER.systemChat,world,ServerSingletons.getPlayer(identity)));
            CommandSyntaxException e;
            if(results.getReader().canRead()) {
                if(results.getExceptions().size() == 1)
                    e = results.getExceptions().values().iterator().next();
                else
                    e = results.getContext().getRange().isEmpty() ? CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(results.getReader()) : CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(results.getReader());

                throw e;
            }
           clientDispatcher.execute(message.substring(1), new ParadoxCommandSource(ServerSingletons.getAccount(identity), ServerSingletons.SERVER.systemChat, world, ServerSingletons.getPlayer(identity)));
        } catch (CommandSyntaxException e) {
            if(OP_LIST.hasAccount(ServerSingletons.SERVER.contextToIdentity.get(ctx).getAccount())){
                parseOperatorCommand(packet,message,identity,ctx);
                return;
            }else {
                var pack = new MessagePacket("[Server] " + e.getRawMessage().getString() + ": " + message);
                pack.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                pack.setupAndSend(ServerSingletons.SERVER.contextToIdentity.get(ctx));
            }
        } catch (IllegalArgumentException e) {
            var pack = new MessagePacket("[Server] "+ e.getMessage());
            pack.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            pack.setupAndSend(ServerSingletons.SERVER.contextToIdentity.get(ctx));
        }

        System.out.println("Player: " + ServerSingletons.getAccount(identity).displayname  + " tried to execute command: " + message.split(" ",2)[0]);
    }
    public static void parseOperatorCommand(GamePacket packet, String message, NetworkIdentity identity, ChannelHandlerContext ctx){
        try {
            ParseResults<CommandSource> results = CommandManager.CONSOLE_DISPATCHER.parse(message.substring(1),new ParadoxCommandSource(ServerSingletons.getAccount(identity),ServerSingletons.SERVER.systemChat, world,null));
            CommandSyntaxException e;
            if(results.getReader().canRead()) {
                if(results.getExceptions().size() == 1)
                    e = results.getExceptions().values().iterator().next();
                else
                    e = results.getContext().getRange().isEmpty() ? CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(results.getReader()) : CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(results.getReader());

                throw e;
            }
            CommandManager.CONSOLE_DISPATCHER.execute(message.substring(1), new ParadoxCommandSource(ServerSingletons.getAccount(identity), ServerSingletons.SERVER.systemChat, world, null));
        } catch (CommandSyntaxException e) {
            ServerSingletons.SERVER.broadcastAsServerExcept(packet,identity);
            var pack = new MessagePacket("[Server] "+ e.getRawMessage().getString() + ": " + message.substring(1));
            pack.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            pack.setupAndSend(ServerSingletons.SERVER.contextToIdentity.get(ctx));
        } catch (IllegalArgumentException e) {
            var pack = new MessagePacket("[Server] "+ e.getMessage());
            pack.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            pack.setupAndSend(ServerSingletons.SERVER.contextToIdentity.get(ctx));
        }
        System.out.println("Player: " + ServerSingletons.getAccount(identity).displayname  + " tried to execute command: " + message.split(" ",2)[0]);

    }

}

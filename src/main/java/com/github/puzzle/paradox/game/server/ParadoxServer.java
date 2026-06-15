package com.github.puzzle.paradox.game.server;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.PuzzleConsoleCommandSource;
import com.github.puzzle.paradox.api.commands.CommandSource;
import com.github.puzzle.paradox.game.command.console.Save;
import com.github.puzzle.paradox.util.AnsiColours;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.server.ServerLauncher;
import finalforeach.cosmicreach.singletons.GameSingletonPlayers;
import finalforeach.cosmicreach.util.Threads;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static finalforeach.cosmicreach.singletons.GameSingletons.world;

public class ParadoxServer {

    public ChannelFuture channelfuture;
    public EventLoopGroup eventloopgroup;
    public EventLoopGroup eventloopgroup1;
    protected int port;
    public int getPort() {
        return this.port;
    }

    protected final static Logger LOGGER = LoggerFactory.getLogger("Cosmic Reach | Server");

    protected void betterServerStop(){
        for (var id : ServerSingletons.SERVER.authenticatedConnections){
            ServerSingletons.SERVER.kick("Server stopping!",id);
        }
        for (var id : ServerSingletons.SERVER.unauthenticatedConnections){
            ServerSingletons.SERVER.kick("Server stopping!",id);
        }
        Save.shouldSave = true;
        GameSingletonPlayers.unregisterAllPlayers();
        synchronized (ChunkSaver.class){
            while (ChunkSaver.isSaving){


            }
            TerminalConsoleAppender.print("stopping"+ "\n");
            ServerLauncher.isRunning = false;
            Threads.stopAllThreads();
        }
    }

    protected void ParadoxProccessServerSideCommands(String command){
        try {
            ParseResults<CommandSource> results = CommandManager.CONSOLE_DISPATCHER.parse(command,new PuzzleConsoleCommandSource(ServerSingletons.SERVER.systemChat,world));
            CommandSyntaxException e;
            if(results.getReader().canRead()) {
                if(results.getExceptions().size() == 1)
                    e = results.getExceptions().values().iterator().next();
                else
                    e = results.getContext().getRange().isEmpty() ? CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(results.getReader()) : CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(results.getReader());
                throw e;
            }
            CommandManager.CONSOLE_DISPATCHER.execute(new StringReader(command), new PuzzleConsoleCommandSource(null, world));
        } catch (CommandSyntaxException e) {
            TerminalConsoleAppender.print(e.getRawMessage().getString() + ": "+ AnsiColours.RED + command + AnsiColours.RESET + "\n");
        } catch (IllegalArgumentException e) {
            TerminalConsoleAppender.print(e.getMessage() + "\n");
        }
    }
}

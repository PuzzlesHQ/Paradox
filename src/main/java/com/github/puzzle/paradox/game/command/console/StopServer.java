package com.github.puzzle.paradox.game.command.console;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.game.command.DefaultPuzzleCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.server.ServerLauncher;
import net.minecrell.terminalconsole.TerminalConsoleAppender;

public class StopServer {

    public static class stop extends DefaultPuzzleCommand {
        public stop(){
            registerPermission(100_000_000);
        }

        @Override
        public int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
            for (var id : ServerSingletons.SERVER.authenticatedConnections){
                ServerSingletons.SERVER.kick("Server stopping!",id);
            }
            for (var id : ServerSingletons.SERVER.unauthenticatedConnections){
                ServerSingletons.SERVER.kick("Server stopping!",id);
            }
            Save.shouldSave = true;
            GameSingletons.unregisterAllPlayers();
            synchronized (ChunkSaver.class){
                while (ChunkSaver.isSaving){


                }
                TerminalConsoleAppender.print("stopping"+ "\n");
                ServerLauncher.isRunning = false;
                Threads.stopAllThreads();
            }
            return 0;
        }

        @Override
        public String getName() {
          return "stop";
        }

        @Override
        public String[] getAliases() {
            return new String[0];
        }
    }
}

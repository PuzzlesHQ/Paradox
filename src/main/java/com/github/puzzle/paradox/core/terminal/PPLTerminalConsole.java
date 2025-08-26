package com.github.puzzle.paradox.core.terminal;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.game.commands.PuzzleConsoleCommandSource;
import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.paradox.util.AnsiColours;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.networking.netty.NettyServer;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.server.ServerLauncher;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import static finalforeach.cosmicreach.singletons.GameSingletons.world;

public class PPLTerminalConsole  extends SimpleTerminalConsole {

    NettyServer server;

    public PPLTerminalConsole(NettyServer server){
        this.server = server;
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder){
        return super.buildReader(builder.appName("Puzzle Plugin Loader"));
    }
    @Override
    protected boolean isRunning() {
        return ServerLauncher.isRunning();
    }

    @Override
    protected void runCommand(String command) {
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

    @Override
    protected void shutdown() {

    }
}

package com.github.puzzle.paradox.game.command.console;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.game.command.DefaultPuzzleCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.singletons.GameSingletons;
import net.minecrell.terminalconsole.TerminalConsoleAppender;

public class Save {

    public static class save extends DefaultPuzzleCommand {

        public save() {
            registerPermission(100_000_000);
        }

        @Override
        public int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
            TerminalConsoleAppender.print("save"+ "\n");
            Save.shouldSave = true;
            return 0;
        }

        @Override
        public String getName() {
            return "save";
        }

        @Override
        public String[] getAliases() {
            return new String[0];
        }
    }

    public static boolean shouldSave;
    public static boolean isSaveRequested() {
        if (!GameSingletons.isHost) {
            return false;
        } else {
            return shouldSave;
        }
    }
}

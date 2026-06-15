package com.github.puzzle.paradox.game.command;

import com.github.puzzle.paradox.api.commands.CommandSource;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public abstract class DefaultPuzzleCommand extends ParadoxBrigaderCommand {

    public void registerPermission(int value) {
        registerPermission("default.command",value);
    }

    public abstract int execute(CommandContext<CommandSource> context) throws CommandSyntaxException;
}

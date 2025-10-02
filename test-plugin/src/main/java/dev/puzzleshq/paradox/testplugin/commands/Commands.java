package dev.puzzleshq.paradox.testplugin.commands;


import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.CommandSource;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.puzzleshq.paradox.testplugin.commands.console.Say;

public class Commands {
    public static void registerConsoleCommands(){
        LiteralArgumentBuilder<CommandSource> say = CommandManager.literal("say");
        say.then(CommandManager.argument("txt", StringArgumentType.greedyString())
                .executes(new Say()));
        CommandManager.CONSOLE_DISPATCHER.register(say);
    }
}

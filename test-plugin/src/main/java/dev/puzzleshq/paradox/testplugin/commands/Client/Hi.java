package dev.puzzleshq.paradox.testplugin.commands.Client;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.game.command.DefaultPuzzleCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;

public class Hi extends DefaultPuzzleCommand {
    public Hi() {}

    @Override
    public String getName() {
        return "hi";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        var pack = new MessagePacket("[Server] "+ "hi");
        pack.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
        ServerSingletons.SERVER.broadcastToAll(pack);
        return 0;
    }

}

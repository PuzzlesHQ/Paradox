package com.github.puzzle.paradox.game.command;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.api.Paradox;
import com.github.puzzle.paradox.core.permissions.GlobalPermissions;
import com.github.puzzle.paradox.game.command.chat.*;
import com.github.puzzle.paradox.game.command.console.*;
import com.github.puzzle.paradox.game.server.ParadoxServerSettings;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.chat.commands.*;
import finalforeach.cosmicreach.chat.commands.moderation.*;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.networking.server.ServerZoneLoader;
import finalforeach.cosmicreach.util.exceptions.ChatCommandException;
import finalforeach.cosmicreach.world.World;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

import static com.github.puzzle.paradox.core.PuzzlePL.SERVER_ACCOUNT;
import static com.github.puzzle.paradox.core.PuzzlePL.clientDispatcher;
import static finalforeach.cosmicreach.chat.commands.Command.registerCommand;
import static finalforeach.cosmicreach.networking.server.ServerSingletons.OP_LIST;

public class Commands {

    private static final Logger LOGGER  = LoggerFactory.getLogger("Paradox | command");
    public static void registerVanillaCommands(Supplier<Command> commandSupplier, String commandName, String[] aliases){
        var builder = CommandManager.literal(commandName);


        com.mojang.brigadier.Command<CommandSource> command2 = (commandContext -> {
            final Account account = commandContext.getSource().getAccount();
            final IChat chat = commandContext.getSource().getChat();
            if(account != null) {
                if (!GlobalPermissions.getPlayerPermissions(account.getUniqueId()).hasPermission("default.command." + commandName) && !account.isOperator()) {
                    var packet = new MessagePacket("You do not have permission to use this command");
                    packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
                    packet.setupAndSend(
                            ServerSingletons
                                    .getIdentityByAccount(account));
                    return 0;
                }
            }
            final World world = commandContext.getSource().getWorld();

            String[] args = commandContext.getInput().substring(1).split(" ");
            String commandStr = args[0];
            if (!commandStr.equalsIgnoreCase("help") && !commandStr.equals("?")) {
                if (commandSupplier != null) {
                    try {
                        Command command = commandSupplier.get();
                        command.setup(account,args);
                        command.run(chat);
                    } catch (ChatCommandException var8) {
                        ChatCommandException cce = var8;
                        chat.addMessage(account, "ERROR: " + cce.getMessage());
                    } catch (Exception var9) {
                        Exception ex = var9;
                        ex.printStackTrace();
                        chat.addMessage(account, "ERROR: An exception occured running the command: " + String.join(" ", args));
                    }

                } else {
                    chat.addMessage(account, "Unknown command: " + commandStr);
                }
            }
            return 0;
        });
        builder.executes(command2);
        builder.then(CommandManager.argument("vanillaCommandArgument", StringArgumentType.greedyString()).executes(command2));
        if(commandSupplier.get().doesRequireOP()){
            var node = CommandManager.CONSOLE_DISPATCHER.register(builder);
            for (var alias : aliases){
                CommandManager.CONSOLE_DISPATCHER.register(CommandManager.literal(alias).redirect(node));
            }
        }else {
            var node = com.github.puzzle.paradox.core.PuzzlePL.clientDispatcher.register(builder);
            for (var alias : aliases) {
                com.github.puzzle.paradox.core.PuzzlePL.clientDispatcher.register(CommandManager.literal(alias).redirect(node));
            }
        }

    }
    static void vanillaCommands(){
        registerCommand(CommandKill::new, "kill");
        registerCommand(CommandGamemode::new, "gamemode", "gm");
        registerCommand(CommandNightVision::new, "nightvision", "nv");
        registerCommand(CommandNoClip::new, "noclip", "nc");
        registerCommand(CommandSkylight::new, "skylight", "sl");
        registerCommand(CommandSummon::new, "summon", "s");
        registerCommand(CommandTeleport::new, "teleport", "tp");
        registerCommand(CommandTime::new, "time", "t");
        registerCommand(CommandBan::new, "ban");
        registerCommand(CommandBanIp::new, "ban-ip", "banip");
        registerCommand(CommandOp::new, "op");
        registerCommand(CommandDeop::new, "deop", "de-op");
        registerCommand(CommandKick::new, "kick");
        registerCommand(CommandUnban::new, "unban");
        registerCommand(CommandClear::new, "clear");
        registerCommand(CommandUnbanIp::new, "unban-ip", "unbanip");
        //registerCommand(CommandSay::new, "say"); // ours is better :)
        //registerCommand(CommandWho::new, "who");
        //registerCommand(CommandPlayerCount::new, "playercount", "pc");
    }
    public static void registerConsoleCommands(){

        CommandManager.CONSOLE_DISPATCHER.register(CommandManager.literal("setrenderdistance").then(
                CommandManager.argument("size", IntegerArgumentType.integer(3,32)).executes(
                        context ->{
                            ServerZoneLoader.INSTANCE.serverLoadDistance = IntegerArgumentType.getInteger(context, "size");
                            return 0;
                        }
                )
        ));

        LiteralArgumentBuilder<CommandSource> say = CommandManager.literal("say");
        say.then(CommandManager.argument("txt", StringArgumentType.greedyString())
                .executes(
                        new Say()
                ));

        CommandManager.CONSOLE_DISPATCHER.register(say);

        LiteralArgumentBuilder<CommandSource> save = CommandManager.literal("save");
        save.executes(new Save.save());

        CommandManager.CONSOLE_DISPATCHER.register(save);

        CommandManager.CONSOLE_DISPATCHER.register(CommandManager.literal("c4grief").executes(context ->{
            ParadoxServerSettings.doesC4Explode  = !ParadoxServerSettings.doesC4Explode;
            System.out.println("Set doesC4Explode to: " + ParadoxServerSettings.doesC4Explode);
            return 0;
        }));

        CommandManager.CONSOLE_DISPATCHER.register(CommandManager.literal("help").executes(context ->{
            if(context.getSource().getAccount() == null)
            {
                StringBuilder builder = new StringBuilder();
                builder.append("Operator Commands:\n");
                Map<CommandNode<CommandSource>, String> mapconsole = CommandManager.CONSOLE_DISPATCHER.getSmartUsage(CommandManager.CONSOLE_DISPATCHER.getRoot(), context.getSource());
                for(String s : mapconsole.values()) {
                    builder.append("\t" + s + "\n");
                }
                TerminalConsoleAppender.print(builder.toString());

            }

            return 0;
        }));

        LiteralArgumentBuilder<CommandSource> setSpawn = CommandManager.literal("setspawn");
        setSpawn.then(CommandManager.argument("x", FloatArgumentType.floatArg())
                .then(CommandManager.argument("y", FloatArgumentType.floatArg())
                        .then(CommandManager.argument("z", FloatArgumentType.floatArg())
                                .executes(new Spawn.SetSpawn()))));

        CommandManager.CONSOLE_DISPATCHER.register(setSpawn);

        LiteralArgumentBuilder<CommandSource> getspawn = CommandManager.literal("getspawn");
        getspawn.executes(new Spawn.GetSpawn());

        CommandManager.CONSOLE_DISPATCHER.register(getspawn);

        LiteralArgumentBuilder<CommandSource> stop = CommandManager.literal("stop");
        stop.executes(new StopServer.stop());

        CommandManager.CONSOLE_DISPATCHER.register(stop);

        LiteralArgumentBuilder<CommandSource> perms = CommandManager.literal("perms");
        perms.then(CommandManager.argument("args",StringArgumentType.greedyString()).executes(new Perms()));
        CommandManager.CONSOLE_DISPATCHER.register(perms);
        vanillaCommands();
    }
    public static void registerClientCommands(){
        LiteralArgumentBuilder<CommandSource> setname = CommandManager.literal("setname");
        setname.then(CommandManager.argument("name", StringArgumentType.word())
                //TODO parse some special chars e.g invis
                .executes(new SetName()));
        clientDispatcher.register(setname);

        LiteralArgumentBuilder<CommandSource> msg = CommandManager.literal("msg");
        msg.then(CommandManager.argument("name", StringArgumentType.word())
                        .then(CommandManager.argument("msg",StringArgumentType.greedyString())
                        .executes(new Msg())));
        clientDispatcher.register(msg);

        LiteralArgumentBuilder<CommandSource> playerlist = CommandManager.literal("playerlist");
        playerlist.executes(context -> {
            //move to file for perms;
            StringBuilder builder = new StringBuilder();
            builder.append(Paradox.getInstance().getPlayers().size() + " player(s) online\n");
            builder.append("player(s):\n");
            for (var plr : Paradox.getInstance().getPlayers()) {
                var acc = plr.getAccount();
                if(acc == context.getSource().getAccount().getParadoxAccount()){
                    builder.append("\t" + acc.getDisplayName() + " <- you\n");
                }else {
                    builder.append("\t" + acc.getDisplayName() + "\n");
                }
            }

            var packet = new MessagePacket(builder.toString());
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(context.getSource().getAccount()));
            return 0;
        });

        clientDispatcher.register(playerlist);
        LiteralArgumentBuilder<CommandSource> playercount = CommandManager.literal("playercount");
        playercount.executes(context -> {
            //move to file for perms;

            var packet = new MessagePacket(Paradox.getInstance().getPlayers().size() + " player(s) online\n");
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(    
                    ServerSingletons
                            .getIdentityByAccount(context.getSource().getAccount()));
            return 0;
        });

        clientDispatcher.register(playercount);
        clientDispatcher.register(CommandManager.literal("help").executes(context ->{
            //move to file for perms;
            Map<CommandNode<CommandSource>, String> map = clientDispatcher.getSmartUsage(clientDispatcher.getRoot(), context.getSource());
            StringBuilder builder = new StringBuilder();
            builder.append("Server Commands:\n");
            for(String s : map.values()) {
                builder.append("\t" + s + "\n");
            }
            if(OP_LIST.hasAccount(context.getSource().getAccount()))
            {
                builder.append("Operator Commands:\n");
                Map<CommandNode<CommandSource>, String> mapconsole = CommandManager.CONSOLE_DISPATCHER.getSmartUsage(CommandManager.CONSOLE_DISPATCHER.getRoot(), context.getSource());
                for(String s : mapconsole.values()) {
                    if(!s.startsWith("help"))
                        builder.append("\t" + s + "\n");
                }
            }

            var packet = new MessagePacket(builder.toString());
            packet.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
            packet.setupAndSend(
                    ServerSingletons
                            .getIdentityByAccount(context.getSource().getAccount()));
            return 0;
        }));


        LiteralArgumentBuilder<CommandSource> tpr = CommandManager.literal("tpr");
        tpr.then(CommandManager.argument("name", StringArgumentType.greedyString())
                .executes(new Teleport.TPR()));

        clientDispatcher.register(tpr);

        LiteralArgumentBuilder<CommandSource> tpa = CommandManager.literal("tpa");
        tpa.executes(new Teleport.TPA());

        clientDispatcher.register(tpa);
    }
}

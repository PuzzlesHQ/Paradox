package com.github.puzzle.paradox.api;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.api.enums.CommandType;
import com.github.puzzle.paradox.api.impl.entity.util.ParadoxEntityUtil;
import com.github.puzzle.paradox.api.interfaces.IParadox;
import com.github.puzzle.paradox.api.interfaces.entity.util.IParadoxEntityUtil;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import com.github.puzzle.paradox.core.PuzzlePL;
import com.github.puzzle.paradox.api.records.Version;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.entities.player.Player;
import net.neoforged.bus.api.BusBuilder;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.puzzle.game.commands.CommandManager.CONSOLE_DISPATCHER;
import static finalforeach.cosmicreach.singletons.GameSingletonPlayers.playersToUniqueIds;

public class Paradox implements IParadox {

    IEventBus eventBus;
    private Paradox(){
        eventBus = BusBuilder.builder().build();
    }
    public static IParadox INSTANCE = null;

    public static IParadox getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Paradox();
        return INSTANCE;
    }

    public IEventBus getEventBus(){
        return eventBus;
    }
    private final Map<String, IParadoxPlayer> cachedPlr = new HashMap<>();
    private final IParadoxEntityUtil entityUtil = new ParadoxEntityUtil();
    ///////////////////////////////////////////////////////////////////////////////////////
    //TODO: improve these so they are not visible in normal API

    /**
     * Internal use only!!! Removes a specific cached ParadoxPlayer
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @param player
     * @see Player
     */
    public void removePlayer(Player player){
        cachedPlr.remove(playersToUniqueIds.get(player));
    }

    /**
     * Internal use only!!! Adds a ParadoxPlayer to cache from Player
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @param player
     * @see Player
     */
    public void addPlayer(Player player){
        cachedPlr.put(player.getAccount().getUniqueId(),player.getParadoxPlayer());
    }

    /**
     * Internal use only!!! Removes all cached ParadoxPlayers
     * @author repletsin5
     * @since API 1.0.0-Alpha
     */
    public void removeAllPlayers(){
        cachedPlr.clear();
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * This returns a set of all players registered on the server
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxPlayer
     * @see Set
     */
    @Override
    public Set<IParadoxPlayer> getPlayers(){
      return new HashSet<>(cachedPlr.values());
    }

    @Override
    public IParadoxEntityUtil getEntityUtil() {
        return entityUtil;
    }

    /**
     * This returns the current API version
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Version
     */
    @Override
    public Version getAPIVersion(){
        return PuzzlePL.API_VERSION;
    }

    @SuppressWarnings("unchecked")
    public void registerCommand(LiteralArgumentBuilder<? extends CommandSource> command,@NotNull CommandType type){
        switch (type){
            case CONSOLE -> {
              CONSOLE_DISPATCHER.register((LiteralArgumentBuilder<CommandSource>) command);
            }
            case CHAT -> {
                PuzzlePL.clientDispatcher.register((LiteralArgumentBuilder<CommandSource>)command);
            }
            case CHAT_CONSOLE -> {
                PuzzlePL.clientDispatcher.register((LiteralArgumentBuilder<CommandSource>)command);
                CONSOLE_DISPATCHER.register((LiteralArgumentBuilder<CommandSource>) command);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}

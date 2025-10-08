package com.github.puzzle.paradox.api.interfaces;

import com.github.puzzle.paradox.api.interfaces.entity.util.IParadoxEntityUtil;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import com.github.puzzle.paradox.api.records.Version;
import net.neoforged.bus.api.IEventBus;

import java.util.Set;

public interface IParadox {

    IEventBus getEventBus();

    /**
     * This returns a set of all players registered on the server
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxPlayer
     * @see Set
     */
    Set<IParadoxPlayer> getPlayers();


    /**
     * This returns entity util class instance
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxEntityUtil
     */
    IParadoxEntityUtil getEntityUtil();
    /**
     * This returns the current API version
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Version
     */
    Version getAPIVersion();
//    @SuppressWarnings("unchecked")
//    public void registerCommand(LiteralArgumentBuilder<? extends CommandSource> command, @NotNull CommandType type);
}

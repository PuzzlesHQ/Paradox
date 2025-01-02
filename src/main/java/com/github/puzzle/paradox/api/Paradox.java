package com.github.puzzle.paradox.api;

import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import com.github.puzzle.paradox.core.PuzzlePL;
import com.github.puzzle.paradox.loader.Version;
import finalforeach.cosmicreach.entities.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Paradox  {

    private Paradox(){

    }
    public static Paradox INSTANCE = null;
    public static Paradox getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Paradox();
        return INSTANCE;
    }

    private final Map<String, ParadoxPlayer> cachedPlr = new HashMap<>();

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
        cachedPlr.remove(player.getAccount().getUniqueId());
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
     * @see ParadoxPlayer
     * @see Set
     */
    public Set<ParadoxPlayer> getPlayers(){
      return new HashSet<>(cachedPlr.values());
    }

    /**
     * This returns the current API version
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Version
     */
    public Version getAPIVersion(){
        return PuzzlePL.API_VERSION;
    }
}

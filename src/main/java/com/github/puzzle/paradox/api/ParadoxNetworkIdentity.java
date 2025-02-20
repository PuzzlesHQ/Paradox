package com.github.puzzle.paradox.api;

import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.server.ServerIdentity;

public class ParadoxNetworkIdentity {
    ServerIdentity identity;
    ParadoxPlayer player;
    public ParadoxNetworkIdentity(ServerIdentity identity) {
        this.identity = identity;
    }

    /**
     * Returns associated ParadoxPlayer with this identity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ParadoxPlayer
     */
    public ParadoxPlayer getPlayer(){
        if(player == null)
            this.player = ClassConverter.convertPlayer(identity.getPlayer());
        if(player == null)
            throw new RuntimeException("Can't get player. please report this to the dev(s)");
        return player;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal network identity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ServerIdentity
     */
    public ServerIdentity getInternalNetworkIdentity(){
        return identity;
    }
}

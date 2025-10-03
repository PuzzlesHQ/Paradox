package com.github.puzzle.paradox.api.interfaces;

import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;

public interface IParadoxNetworkIdentity {

    /**
     * Returns associated ParadoxPlayer with this identity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxPlayer
     */
    IParadoxPlayer getPlayer();

}

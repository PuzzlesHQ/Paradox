package com.github.puzzle.paradox.game.player;

import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.entities.player.Player;

public abstract class InternalParadoxPlayer {
    transient IParadoxPlayer paradoxPlayer = null;

    public IParadoxPlayer getParadoxPlayer(){
        if(paradoxPlayer == null){
            paradoxPlayer = ClassConverter.convertPlayer((Player)(Object)this);
        }
        return paradoxPlayer;
    }
}

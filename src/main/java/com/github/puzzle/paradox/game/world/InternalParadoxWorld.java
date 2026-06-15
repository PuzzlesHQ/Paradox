package com.github.puzzle.paradox.game.world;

import com.github.puzzle.paradox.api.interfaces.IParadoxWorld;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.world.World;

public class InternalParadoxWorld {

    transient IParadoxWorld paradoxWorld = null;

    public IParadoxWorld getParadoxWorld(){
        if(paradoxWorld == null){
            paradoxWorld = ClassConverter.convertClass((World)(Object)this);
        }
        return paradoxWorld;
    }
}

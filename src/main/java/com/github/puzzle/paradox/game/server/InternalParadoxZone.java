package com.github.puzzle.paradox.game.server;

import com.github.puzzle.paradox.api.interfaces.IParadoxZone;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.world.Zone;

public class InternalParadoxZone {
    IParadoxZone paradoxZone;

    public IParadoxZone getParadoxZone(){
        if(paradoxZone == null){
            paradoxZone = ClassConverter.convertClass((Zone)(Object)this);
        }
        return paradoxZone;
    }
}

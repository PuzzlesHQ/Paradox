package com.github.puzzle.paradox.api.impl.world;

import com.github.puzzle.paradox.api.interfaces.IParadoxWorld;
import finalforeach.cosmicreach.world.World;

public class ParadoxWorld implements IParadoxWorld {

    World world;

    public ParadoxWorld(World world){
        this.world = world;
    }


    public World getInternalWorld(){
        return world;
    }
}

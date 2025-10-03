package com.github.puzzle.paradox.game.entity;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxEntity;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.entities.Entity;

public class InternalParadoxEntity {
    transient IParadoxEntity entity = null;

    public IParadoxEntity getParadoxEntity(){
        if(entity == null){
            entity = ClassConverter.convertEntity((Entity)(Object)this);
        }
        return entity;
    }
}

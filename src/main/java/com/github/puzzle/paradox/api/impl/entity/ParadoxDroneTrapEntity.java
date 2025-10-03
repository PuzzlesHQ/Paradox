package com.github.puzzle.paradox.api.impl.entity;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxDroneTrapEntity;
import com.github.puzzle.paradox.api.interfaces.entity.IParadoxPlayerEntity;
import finalforeach.cosmicreach.entities.mobs.MobDroneTrap;

public class ParadoxDroneTrapEntity extends ParadoxEntity implements IParadoxDroneTrapEntity {

    MobDroneTrap trapEntity;

    public ParadoxDroneTrapEntity(MobDroneTrap entity) {
        super(entity);
        trapEntity = entity;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal drone trap entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see MobDroneTrap
     */
    public MobDroneTrap getInternalDroneTrapEntity(){
        return trapEntity;
    }
}

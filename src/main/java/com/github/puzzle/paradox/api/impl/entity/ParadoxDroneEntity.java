package com.github.puzzle.paradox.api.impl.entity;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.interfaces.entity.IParadoxDroneEntity;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.mobs.MobInterceptor;

public class ParadoxDroneEntity extends ParadoxEntity implements IParadoxDroneEntity {

    MobInterceptor droneEntity;
    public ParadoxDroneEntity(MobInterceptor entity) {
        super(entity);
        droneEntity = entity;
    }

    /**
     * Returns a bool of this drone's friend status
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    public boolean isFriendly(){
        return this.droneEntity.isFriendly();
    }
    /**
     * Avoid using this. Returns Cosmic Reach's internal drone entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see MobInterceptor
     * @see Entity
     */
    public MobInterceptor getInternalDroneEntity() {
        return droneEntity;
    }
}

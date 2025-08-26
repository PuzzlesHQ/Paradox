package com.github.puzzle.paradox.api.entity;

import finalforeach.cosmicreach.entities.mobs.MobLaserInterceptor;

public class ParadoxLaserDroneEntity extends ParadoxEntity {
    MobLaserInterceptor laserDroneEntity;

    public ParadoxLaserDroneEntity(MobLaserInterceptor entity) {
        super(entity);
        laserDroneEntity = entity;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal laser drone entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see MobLaserInterceptor
     */
    public MobLaserInterceptor getInternalEntityLaserProjectile(){
        return laserDroneEntity;
    }
}

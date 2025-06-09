package com.github.puzzle.paradox.api.entity;

import finalforeach.cosmicreach.entities.LaserDroneEntity;

public class ParadoxLaserDroneEntity extends ParadoxEntity {
    LaserDroneEntity laserDroneEntity;

    public ParadoxLaserDroneEntity(LaserDroneEntity entity) {
        super(entity);
        laserDroneEntity = entity;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal laser drone entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see LaserDroneEntity
     */
    public LaserDroneEntity getInternalEntityLaserProjectile(){
        return laserDroneEntity;
    }
}

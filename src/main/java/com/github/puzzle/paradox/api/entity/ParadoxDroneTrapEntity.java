package com.github.puzzle.paradox.api.entity;

import finalforeach.cosmicreach.entities.DroneTrapEntity;

public class ParadoxDroneTrapEntity extends ParadoxEntity {

    DroneTrapEntity trapEntity;

    public ParadoxDroneTrapEntity(DroneTrapEntity entity) {
        super(entity);
        trapEntity = entity;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal drone trap entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see DroneTrapEntity
     */
    public DroneTrapEntity getInternalDroneTrapEntity(){
        return trapEntity;
    }
}

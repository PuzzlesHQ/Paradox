package com.github.puzzle.paradox.api.entity;

import finalforeach.cosmicreach.entities.EntityLaserProjectile;

public class ParadoxLaserProjectileEntity extends ParadoxEntity {

    EntityLaserProjectile projectile;
    public ParadoxLaserProjectileEntity(EntityLaserProjectile entity) {
        super(entity);
        projectile = entity;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal laser projectile entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see EntityLaserProjectile
     */
    public EntityLaserProjectile getInternalEntityLaserProjectile(){
        return projectile;
    }
}

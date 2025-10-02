package com.github.puzzle.paradox.api.entity;


import finalforeach.cosmicreach.entities.projectiles.EntityProjectileLaser;

public class ParadoxLaserProjectileEntity extends ParadoxEntity {

    EntityProjectileLaser projectile;
    public ParadoxLaserProjectileEntity(EntityProjectileLaser entity) {
        super(entity);
        projectile = entity;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal laser projectile entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see EntityProjectileLaser
     */
    public EntityProjectileLaser getInternalEntityLaserProjectile(){
        return projectile;
    }
}

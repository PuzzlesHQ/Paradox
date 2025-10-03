package com.github.puzzle.paradox.api.impl.entity;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxIncinerator;
import finalforeach.cosmicreach.entities.mobs.MobIncinerator;

public class ParadoxIncinerator extends ParadoxEntity implements IParadoxIncinerator {
    MobIncinerator incinerator;

    public ParadoxIncinerator(MobIncinerator entity) {
        super(entity);
        incinerator = entity;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal incinerator entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see MobIncinerator
     */
    public MobIncinerator getInternalEntityIncinerator(){
        return incinerator;
    }
}

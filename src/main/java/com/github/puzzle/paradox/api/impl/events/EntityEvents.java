package com.github.puzzle.paradox.api.impl.events;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public abstract class EntityEvents extends Event {

    public EntityEvents(IParadoxEntity entity){
        this.entity = entity;
    }

    private final IParadoxEntity entity;

    public IParadoxEntity getEntity() {
        return entity;
    }

    public static class OnEntitySpawn extends EntityEvents implements ICancellableEvent {

        public OnEntitySpawn(IParadoxEntity entity) {
            super(entity);
        }
    }

    public enum DamageSource {
        PLAYER,
        MOB,
        ENVIRONMENT
    }

    //TODO: impl source and entitySource
    public static class OnTakeDamage extends EntityEvents implements ICancellableEvent {

        public OnTakeDamage(IParadoxEntity entity, @Nullable DamageSource source, float hitPoints, @Nullable IParadoxEntity entitySource) {
            super(entity);
            this.damageDealt = hitPoints;
            this.source = source;
            this.entitySource = entitySource;
        }
        private final float damageDealt;
        private final DamageSource source;
        private final IParadoxEntity entitySource;


        /**
         * Returns a float of entity's received damage
         * @author repletsin5
         * @since API 1.0.0-Alpha
         */
        public float getDamageDealt() {
            return damageDealt;
        }

        /**
         * Do not use, not implemented
         */
        public @Nullable DamageSource getSource() {
            throw new NotImplementedException();
//            return source;
        }

        /**
         * Do not use, not implemented
         */
        public @Nullable IParadoxEntity getEntitySource() {
            throw new NotImplementedException();
            //return entitySource;
        }
    }
}

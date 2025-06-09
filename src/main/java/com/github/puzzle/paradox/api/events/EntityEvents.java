package com.github.puzzle.paradox.api.events;

import com.github.puzzle.paradox.api.entity.ParadoxEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public abstract class EntityEvents extends Event {

    public EntityEvents(ParadoxEntity entity){
        this.entity = entity;
    }

    private final ParadoxEntity entity;

    public ParadoxEntity getEntity() {
        return entity;
    }

    public static class OnEntitySpawn extends EntityEvents implements ICancellableEvent {

        public OnEntitySpawn(ParadoxEntity entity) {
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

        public OnTakeDamage(ParadoxEntity entity, @Nullable DamageSource source, float hitPoints, @Nullable ParadoxEntity entitySource) {
            super(entity);
            this.damageDealt = hitPoints;
            this.source = source;
            this.entitySource = entitySource;
        }
        private final float damageDealt;
        private final DamageSource source;
        private final ParadoxEntity entitySource;


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
        public @Nullable ParadoxEntity getEntitySource() {
            throw new NotImplementedException();
            //return entitySource;
        }
    }
}

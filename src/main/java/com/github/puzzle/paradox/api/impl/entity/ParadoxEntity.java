package com.github.puzzle.paradox.api.impl.entity;


import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.Paradox;
import com.github.puzzle.paradox.api.impl.entity.util.ParadoxEntityUtil;
import com.github.puzzle.paradox.api.interfaces.entity.IParadoxEntity;
import com.github.puzzle.paradox.core.PuzzlePL;
import finalforeach.cosmicreach.entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;



@SuppressWarnings("unchecked")
public class ParadoxEntity implements IParadoxEntity {




    public ParadoxEntity(Entity entity) {
        this.entity = entity;
        this.uuid = UUID.randomUUID();
        ((ParadoxEntityUtil) Paradox.getInstance().getEntityUtil()).uuidToParadoxEntity.put(uuid,this);
    }
    Entity entity;
    UUID uuid;
    /**
     * Avoid using this. Returns Cosmic Reach's internal entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Entity
     */
    public Entity getInternalEntity() {
        return entity;
    }

    /**
     * Returns a Vector3 of entity's position
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    public Vector3 getPosition() {
        return entity.getPosition();
    }

    /**
     * Sets the entity's position via a {@link Vector3}
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    public void setPosition(@NotNull Vector3 position) {
        entity.setPosition(position);
    }

    /**
     * Returns the UUID of the entity as a String
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see UUID
     * @see String
     */
    public String getUUIDAsString() {
        return uuid.toString();
    }

    /**
     * Returns the UUID of the entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see UUID
     */
    public UUID getUUID(){
        return uuid;
    }

    /**
     * Returns a Vector3 of entity's direction it is looking
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    public Vector3 getViewDirection(){
        return this.entity.viewDirection;
    }


}

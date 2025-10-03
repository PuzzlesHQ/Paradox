package com.github.puzzle.paradox.api.interfaces.entity;


import com.badlogic.gdx.math.Vector3;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public interface IParadoxEntity {
    UUID uuid = null;

    /**
     * Returns a Vector3 of entity's position
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    Vector3 getPosition();

    /**
     * Sets the entity's position via a {@link Vector3}
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    void setPosition(@NotNull Vector3 position);

    /**
     * Returns the UUID of the entity as a String
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see UUID
     * @see String
     */
    default String getUUIDAsString() {
        return uuid.toString();
    }

    /**
     * Returns the UUID of the entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see UUID
     */
    default UUID getUUID(){
        return uuid;
    }

    /**
     * Returns a Vector3 of entity's direction it is looking
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    Vector3 getViewDirection();


}

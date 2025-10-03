package com.github.puzzle.paradox.api.interfaces;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxEntity;
import org.jetbrains.annotations.NotNull;

public interface IParadoxZone {

    /**
     * Add the provided entity to the zone
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxEntity
     */
    public void addEntity(@NotNull IParadoxEntity entity);
}

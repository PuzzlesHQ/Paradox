package com.github.puzzle.paradox.api.interfaces.entity.util;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.interfaces.IParadoxZone;
import com.github.puzzle.paradox.api.interfaces.entity.IParadoxEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IParadoxEntityUtil {
    /**
     * Returns a spawned instance of the proved paradox entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @param eClass
     * @param position
     * @param zone
     * @see IParadoxEntity
     */
    <E extends IParadoxEntity> E spawnEntity(@NotNull Class<E> eClass, @NotNull Vector3 position, @NotNull IParadoxZone zone);

    String getEntityIDByClass(@NotNull Class<? extends IParadoxEntity> e);

    IParadoxEntity getByUUID(@NotNull UUID uuid);
}

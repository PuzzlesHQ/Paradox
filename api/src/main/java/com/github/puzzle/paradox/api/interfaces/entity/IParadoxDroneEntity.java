package com.github.puzzle.paradox.api.interfaces.entity;

import com.badlogic.gdx.math.Vector3;

public interface IParadoxDroneEntity extends IParadoxEntity {


    /**
     * Returns a bool of this drone's friend status
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    boolean isFriendly();

}

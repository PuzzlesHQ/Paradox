package com.github.puzzle.paradox.api.interfaces.entity;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import org.jetbrains.annotations.NotNull;

public interface IParadoxPlayerEntity extends IParadoxEntity {


    /**
     * Returns the player of the player entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxPlayer
     */
    IParadoxPlayer getPlayer();


}

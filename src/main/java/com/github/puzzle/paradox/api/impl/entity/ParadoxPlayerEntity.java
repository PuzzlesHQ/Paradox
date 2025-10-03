package com.github.puzzle.paradox.api.impl.entity;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.interfaces.entity.IParadoxPlayerEntity;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxAccount;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import finalforeach.cosmicreach.entities.player.PlayerEntity;

public class ParadoxPlayerEntity extends ParadoxEntity implements IParadoxPlayerEntity  {

    PlayerEntity playerEntity;
    public ParadoxPlayerEntity(PlayerEntity playerEntity) {
        super(playerEntity);
        this.playerEntity = playerEntity;
    }
    /**
     * Returns the player of the player entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxPlayer
     */
    public IParadoxPlayer getPlayer() {
        return playerEntity.player.getParadoxPlayer();
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal player entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see PlayerEntity
     */
    public PlayerEntity getInternalPlayerEntity(){
        return playerEntity;
    }


}

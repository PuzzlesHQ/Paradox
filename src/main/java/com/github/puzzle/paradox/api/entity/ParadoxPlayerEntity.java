package com.github.puzzle.paradox.api.entity;

import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import finalforeach.cosmicreach.entities.player.PlayerEntity;

public class ParadoxPlayerEntity extends ParadoxEntity {

    PlayerEntity playerEntity;
    public ParadoxPlayerEntity(PlayerEntity playerEntity) {
        super(playerEntity);
        this.playerEntity = playerEntity;
    }
    /**
     * Returns the player of the player entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ParadoxPlayer
     */
    public ParadoxPlayer getPlayer() {
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

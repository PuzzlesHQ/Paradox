package com.github.puzzle.paradox.api.player;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.entity.ParadoxPlayerEntity;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.entities.player.PlayerEntity;

public class ParadoxPlayer {

    Player player;


    ParadoxPlayerEntity entity;
    Vector3 lastSafePosition;
    long lastBreakTime = 0;
    ParadoxAccount account;
    public ParadoxPlayer(Player player, PlayerEntity playerEntity){
        this.player = player;
        this.entity = (ParadoxPlayerEntity) playerEntity.getParadoxEntity();
        this.account = ClassConverter.convertClass(player.getAccount());
        this.lastSafePosition = getPosition();
    }

    /**
     * Returns the associated epox time this ParadoxPlayer last broke a block
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Long
     */
    public long getLastBreakTime() {
        return lastBreakTime;
    }

    /**
     * Returns the associated gamemode id of this Player
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see String
     */
    public String getGamemode() {
        return player.gamemode.gamemodeId;
    }


    /**
     * Returns the associated epox time of this ParadoxPlayer
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Long
     */
    public void setLastBreakTime(long time) {
        lastBreakTime = time;
    }
//TODO create ParadoxZone
//    public Zone getZone(){
//        return player.getZone();
//    }

    /**
     * Returns the associated ParadoxPlayerEntity of this Player
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ParadoxPlayerEntity
     */
    public ParadoxPlayerEntity getEntity(){
        return entity;
    }

    /**
     * Returns a Vector3 of player's position from its entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     * @see PlayerEntity
     */
    public Vector3 getPosition(){
        return getEntity().getPosition();
    }


    /**
     * Sets a Vector3 of player's position of its entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    public void setPosition(Vector3 position){
        player.getEntity().setPosition(position);
    }
    /**
     * Returns a Vector3 of know location that of the player that has no player check issues
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     * @see PlayerEntity
     */
    public Vector3 getLastSafePosition(){
        return lastSafePosition;
    }
    public void setLastSafePosition(Vector3 position){
        lastSafePosition = position;
    }



    /**
     * Returns the associated account of the player
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ParadoxAccount
     */
    public ParadoxAccount getAccount(){
        return account;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal player class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Player
     */
    public Player getInternalPlayer(){
        return player;
    }
}

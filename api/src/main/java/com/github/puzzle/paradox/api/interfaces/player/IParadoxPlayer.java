package com.github.puzzle.paradox.api.interfaces.player;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.interfaces.entity.IParadoxPlayerEntity;

public interface IParadoxPlayer {



    /**
     * Returns the associated epox time this ParadoxPlayer last broke a block
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Long
     */
    public long getLastBreakTime();

    /**
     * Returns the associated gamemode id of this Player
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see String
     */
    public  String getGamemode() ;


    /**
     * Returns the associated epox time of this ParadoxPlayer
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Long
     */
    public void setLastBreakTime(long time);
//TODO create ParadoxZone
//    public Zone getZone(){
//        return player.getZone();
//    }

    /**
     * Returns the associated ParadoxPlayerEntity of this Player
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxPlayerEntity
     */
    IParadoxPlayerEntity getEntity();

    /**
     * Returns a Vector3 of player's position from its entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    default Vector3 getPosition(){
        return getEntity().getPosition();
    }


    /**
     * Sets a Vector3 of player's position of its entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    default void setPosition(Vector3 position){
        getEntity().setPosition(position);
    }
    /**
     * Returns a Vector3 of know location that of the player that has no player check issues
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Vector3
     */
    Vector3 getLastSafePosition();


    void setLastSafePosition(Vector3 position);



    /**
     * Returns the associated account of the player
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxAccount
     */
    IParadoxAccount getAccount();

}

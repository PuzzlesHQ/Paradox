package com.github.puzzle.paradox.api.events.packet;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.paradox.api.ParadoxNetworkIdentity;
import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.packets.entities.PlayerPositionPacket;
import io.netty.channel.ChannelHandlerContext;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class MiscEvents extends Event {

    GamePacket gamePacket;
    ChannelHandlerContext channel;
    ParadoxNetworkIdentity identity;
    
    public MiscEvents(GamePacket packet, ParadoxNetworkIdentity identity, ChannelHandlerContext channel) {
        this.gamePacket = packet;
        this.channel = channel;
        this.identity = identity;
    }
    /**
     * Returns associated {@link ParadoxPlayer} with the {@link ParadoxNetworkIdentity}
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ParadoxPlayer
     */
    public ParadoxPlayer getPlayer(){
      return getIdentity().getPlayer();
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal game packet class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see GamePacket
     */
    public GamePacket getInternalGamePacket() {
        return gamePacket;
    }
    /**
     * Avoid using this. Returns Cosmic Reach's internal channel handler context class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ChannelHandlerContext
     */
    public ChannelHandlerContext getInternalCtx(){
        return channel;
    }

    /**
     * Returns associated ParadoxNetworkIdentity with this event
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ParadoxNetworkIdentity
     */
    public ParadoxNetworkIdentity getIdentity(){
        return identity;
    }

    public static class OnPlayerPositionPacket extends MiscEvents implements ICancellableEvent {

        public OnPlayerPositionPacket(GamePacket packet, ParadoxNetworkIdentity identity,ChannelHandlerContext channel) {
            super(packet,identity,channel);
        }

        public PlayerPositionPacket getPlayerPositionPacket(){
            return (PlayerPositionPacket)gamePacket;
        }

        /**
         * Returns associated position {@link Vector3 } the player {@link ParadoxPlayer} wants to move to
         * @author repletsin5
         * @since API 1.0.0-Alpha
         * @see Vector3
         */
        public Vector3 getRequestedPosition(){
            return getPlayerPositionPacket().position;
        }
        /**
         * Returns associated look vector {@link Vector3 } the player {@link ParadoxPlayer} moved to
         * @author repletsin5
         * @since API 1.0.0-Alpha
         * @see Vector3
         */
        public Vector3 getRequestedLookVector(){
            return getPlayerPositionPacket().viewDir;
        }
        /**
         * Returns associated players position {@link Vector3 }
         * @author repletsin5
         * @since API 1.0.0-Alpha
         * @see Vector3
         */
        public Vector3 getCurrentPosition(){
            return getPlayer().getPosition();
        }
    }

    public static class OnPacketAct extends MiscEvents {

        public OnPacketAct(GamePacket packet,ParadoxNetworkIdentity identity,ChannelHandlerContext channel) {
            super(packet,identity,channel);
        }

        public GamePacket getPacket(){
            return gamePacket;
        }
    }
}

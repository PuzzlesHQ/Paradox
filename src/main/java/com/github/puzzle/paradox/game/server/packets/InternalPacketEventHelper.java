package com.github.puzzle.paradox.game.server.packets;

import com.github.puzzle.paradox.api.Paradox;
import com.github.puzzle.paradox.api.impl.events.packet.ChatEvents;
import com.github.puzzle.paradox.api.impl.events.packet.MiscEvents;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.packets.entities.PlayerPositionPacket;
import io.netty.channel.ChannelHandlerContext;

public class InternalPacketEventHelper {

    public static boolean fireAssociatedPacketEvent(GamePacket packet, NetworkIdentity identity, ChannelHandlerContext ctx){
        Paradox.getInstance().getEventBus().post(new MiscEvents.OnPacketAct(packet,identity.getParadoxNetworkIdentity(),ctx));
        if(packet instanceof PlayerPositionPacket) {
            return !Paradox.getInstance().getEventBus().post(new MiscEvents.OnPlayerPositionPacket(packet, identity.getParadoxNetworkIdentity(), ctx)).isCanceled();
        }
        if (packet instanceof MessagePacket mp) {
            return !Paradox.getInstance().getEventBus().post(new ChatEvents.OnChatReceive(mp.message,identity.getParadoxNetworkIdentity().getPlayer())).isCanceled();
        }
        return true;
    }
}

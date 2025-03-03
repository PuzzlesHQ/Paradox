package com.github.puzzle.paradox.game.player;

import com.github.puzzle.paradox.api.events.packet.MiscEvents;
import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import com.github.puzzle.paradox.game.server.ParadoxServerSettings;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.entities.player.Gamemode;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import net.neoforged.bus.api.SubscribeEvent;

public class PlayerChecks {

    public static boolean blockChecks(NetworkIdentity identity, BlockPosition blockPosition){
//        playerRaycastBlockHitCheck(identity.getPlayer().getParadoxPlayer(),blockPosition);
        return blockRangeCheck(identity.getPlayer().getParadoxPlayer(),blockPosition);
    }

    public static boolean blockRangeCheck(ParadoxPlayer player, BlockPosition blockPosition){
        var plrPos = player.getPosition();
        float distance = plrPos.dst(blockPosition.getGlobalX(),blockPosition.getGlobalY(),blockPosition.getGlobalZ());
        return ((!(distance > 6.45f)) && ParadoxServerSettings.anticheat); // a little higher than what i saw as the max, but it was with a small test;
    }

    public static boolean blockBreakTimingCheck(ParadoxPlayer player, BlockPosition blockPosition){
//        LoggerFactory.getLogger("Paradox | Player Checks").warn("block hardness: {}",blockPosition.getBlockState().hardness);
        long curtime = System.currentTimeMillis();
        if(player.getLastBreakTime() == 0) {
            player.setLastBreakTime(curtime);
            return true;
        }
        long delta = curtime-player.getLastBreakTime();
//        LoggerFactory.getLogger("Paradox | Player Checks").warn("delta {}",(delta));
        if(player.getGamemode().equals(Gamemode.CREATIVE.gamemodeId)){
            if(delta < ParadoxServerSettings.creativeBreakDelayMin) {
//                LoggerFactory.getLogger("Paradox | Player Checks").warn("too quick in creative {}", (delta));
                return false;
            }
        }else {
            if ((delta) < blockPosition.getBlockState().hardness * 1000) {
                //TODO need to have the ability to check what the player is holding
//                LoggerFactory.getLogger("Paradox | Player Checks").warn("too quick {}", (delta));
//                return false;
            }
        }
        player.setLastBreakTime(curtime);
        return true;
    }
    public static boolean playerRaycastBlockHitCheck(ParadoxPlayer player, BlockPosition blockPosition){
        //TODO: check what block player is looking at
        var plrPos = player.getPosition();
        var plrLookDir = player.getEntity().getViewDirection();
        return false;
    }

    public static class PositionChecks {
        @SubscribeEvent
        public void OnPlayerPosition(MiscEvents.OnPlayerPositionPacket event){
            //TODO
            event.setCanceled(false);
        }

    }
}

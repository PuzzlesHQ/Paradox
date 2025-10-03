package com.github.puzzle.paradox.test;

import com.github.puzzle.paradox.api.impl.events.BlockEventEvents;
import net.neoforged.bus.api.SubscribeEvent;

public class BlockEventTest {

    @SubscribeEvent
    public void onBlockEventAct(BlockEventEvents.OnEventAct event){
        event.setCanceled(false);
    }
}

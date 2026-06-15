package com.github.puzzle.paradox.game.player.chat;

import com.github.puzzle.paradox.api.interfaces.player.IParadoxChat;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.chat.Chat;

public class InternalParadoxChat {
    IParadoxChat paradoxChat = null;

    public IParadoxChat getParadoxChat(){
        if(paradoxChat == null){
            paradoxChat = ClassConverter.convertClass((Chat)(Object)this);
        }
        return paradoxChat;
    }
}

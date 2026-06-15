package com.github.puzzle.paradox.api.impl.player.chat;

import com.github.puzzle.paradox.api.impl.player.ParadoxAccount;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxAccount;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxChat;
import finalforeach.cosmicreach.chat.Chat;

public class ParadoxChat implements IParadoxChat {

    Chat chat;

    public ParadoxChat(Chat chat){
        this.chat = chat;
    }

    public void addMessage(IParadoxAccount account, String message) {
        chat.addMessage(((ParadoxAccount)account).getInternalAccount(),message);
    }

    public Chat getInternalChat(){
        return chat;
    }
}

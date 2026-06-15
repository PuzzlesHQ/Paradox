package com.github.puzzle.paradox.api.commands;


import com.github.puzzle.paradox.api.interfaces.IParadoxNetworkIdentity;
import com.github.puzzle.paradox.api.interfaces.IParadoxWorld;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxAccount;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxChat;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;

public interface CommandSource {
    IParadoxAccount getAccount();
    IParadoxChat getChat();
    IParadoxWorld getWorld();
    IParadoxNetworkIdentity getIdentity();
    IParadoxPlayer getPlayer();
}

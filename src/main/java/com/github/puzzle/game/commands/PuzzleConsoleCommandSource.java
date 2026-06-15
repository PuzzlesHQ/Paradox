package com.github.puzzle.game.commands;

import com.github.puzzle.paradox.api.commands.CommandSource;
import com.github.puzzle.paradox.api.interfaces.IParadoxNetworkIdentity;
import com.github.puzzle.paradox.api.interfaces.IParadoxWorld;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxAccount;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxChat;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;



public class PuzzleConsoleCommandSource implements CommandSource {

    final World world;
    final IChat chat;


    public PuzzleConsoleCommandSource(IChat chat, World world) {
        this.world = world;
        this.chat = chat;
    }

    @Override
    public IParadoxAccount getAccount() {
        return null;
    }

    @Override
    public IParadoxChat getChat() {
        return ((Chat)chat).getParadoxChat();
    }

    @Override
    public IParadoxWorld getWorld() {
        return world.getParadoxWorld();
    }

    @Override
    public IParadoxNetworkIdentity getIdentity() {
        return null;
    }

    @Override
    public IParadoxPlayer getPlayer() {
        return null;
    }

}

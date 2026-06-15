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

public class ParadoxCommandSource implements CommandSource {

    final World world;
    final IChat chat;
    final Account account;
    final Player player;
    public ParadoxCommandSource(Account account, IChat chat, World world, Player player) {
        this.account = account;
        this.chat = chat;
        this.world = world;
        this.player = player;
    }

    @Override
    public IParadoxAccount getAccount() {
        return account.getParadoxAccount();
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
        return account.getPlayer().getParadoxPlayer();
    }

}

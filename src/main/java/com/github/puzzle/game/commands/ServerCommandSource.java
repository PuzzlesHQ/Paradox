package com.github.puzzle.game.commands;

import com.github.puzzle.paradox.api.commands.CommandSource;
import com.github.puzzle.paradox.api.impl.player.ParadoxPlayer;
import com.github.puzzle.paradox.api.interfaces.IParadoxNetworkIdentity;
import com.github.puzzle.paradox.api.interfaces.IParadoxWorld;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxAccount;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxChat;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.singletons.GameSingletonPlayers;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;

/**
 * @see CommandSource
 */
public class ServerCommandSource implements CommandSource {

    final NetworkIdentity identity;
    final World world;
    final IChat chat;
    final boolean hasOperator;

    public ServerCommandSource(
            boolean hasOperator,
            NetworkIdentity identity,
            World world,
            IChat chat
    ) {
        this.identity = identity;
        this.world = world;
        this.chat = chat;
        this.hasOperator = hasOperator;
    }

    @Override
    public IParadoxNetworkIdentity getIdentity() {
        return identity.getParadoxNetworkIdentity();
    }

    @Override
    public IParadoxPlayer getPlayer() {
        return getIdentity().getPlayer();
    }

    @Override
    public IParadoxAccount getAccount() {
        return GameSingletonPlayers.getAccountFromPlayer(((ParadoxPlayer)getIdentity().getPlayer()).getInternalPlayer()).getParadoxAccount();
    }

    @Override
    public IParadoxChat getChat() {
        return ((Chat)chat).getParadoxChat();
    }

    @Override
    public IParadoxWorld getWorld() {
        return world.getParadoxWorld();
    }

    public boolean hasOperator() {
        return hasOperator;
    }
}
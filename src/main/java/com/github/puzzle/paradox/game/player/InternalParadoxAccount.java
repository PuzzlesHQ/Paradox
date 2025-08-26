package com.github.puzzle.paradox.game.player;

import com.github.puzzle.paradox.api.player.ParadoxAccount;
import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;


public abstract class InternalParadoxAccount {
    public String username;
    public String uniqueId;
    public String displayname;
    transient public ParadoxAccount paradoxAccount;

    public boolean isValid = true;
    public boolean tpRequst = false;
    private Player tprPlayer = null;
    private Player tprToPlayer = null;

    public void addTpr(ServerIdentity id, Player playerToTp){
        this.tpRequst = true;
        this.tprPlayer = ServerSingletons.getPlayer(id);
        this.tprToPlayer = playerToTp;
    }

    public ParadoxAccount getParadoxAccount(){
        if(paradoxAccount == null){
            paradoxAccount = ClassConverter.convertClass((Account)(Object)this);
        }
        return paradoxAccount;
    }

    public Player getTprPlayer() {
        return tprPlayer;
    }

    public void resetTPRInfo(){
        this.tpRequst = false;
        this.tprPlayer = null;
        this.tprToPlayer = null;
    }
    public Player getTprToPlayer() {
        return tprToPlayer;
    }
//    public abstract String getPrefix();
}

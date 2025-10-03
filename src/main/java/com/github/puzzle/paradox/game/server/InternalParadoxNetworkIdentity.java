package com.github.puzzle.paradox.game.server;

import com.github.puzzle.paradox.api.interfaces.IParadoxNetworkIdentity;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.networking.NetworkIdentity;

public abstract class InternalParadoxNetworkIdentity {


    private IParadoxNetworkIdentity paradoxNetworkIdentity;
    public boolean waskicked = false;
    public boolean shouldIgnore = false;
    public boolean isOP = false;
    public boolean usingModdedClient = false;
    public String clientName = "unknown";
    public IParadoxNetworkIdentity getParadoxNetworkIdentity(){
        if(paradoxNetworkIdentity == null){
            paradoxNetworkIdentity = ClassConverter.convertClass((NetworkIdentity)(Object)this);
        }
        return paradoxNetworkIdentity;
    }


}

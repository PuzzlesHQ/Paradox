package com.github.puzzle.paradox.api.impl.player;

import com.github.puzzle.paradox.api.interfaces.player.IParadoxAccount;
import finalforeach.cosmicreach.accounts.Account;

public class ParadoxAccount implements IParadoxAccount {

    Account account;
    public ParadoxAccount(Account account){
        this.account = account;
    }

    /**
     * This the account's display name as a String
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see String
     */
    public String getDisplayName(){
        return this.account.displayname;
    }

    @Override
    public void setUsername(String username) {
         account.setUsername(username);
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal account class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Account
     */
    public Account getInternalAccount(){
        return account;
    }
}

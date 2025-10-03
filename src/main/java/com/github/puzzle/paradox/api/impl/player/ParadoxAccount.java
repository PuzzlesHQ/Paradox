package com.github.puzzle.paradox.api.impl.player;

import finalforeach.cosmicreach.accounts.Account;

public class ParadoxAccount {

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

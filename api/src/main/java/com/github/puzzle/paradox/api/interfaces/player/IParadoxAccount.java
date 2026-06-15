package com.github.puzzle.paradox.api.interfaces.player;


public interface IParadoxAccount {

    /**
     * The account's display name as a String
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see String
     */
    String getDisplayName();


    /**
     * Sets the account's username name as a String
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see String
     */
    void setUsername(String username);
}

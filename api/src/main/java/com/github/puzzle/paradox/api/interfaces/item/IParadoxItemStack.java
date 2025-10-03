package com.github.puzzle.paradox.api.interfaces.item;

import org.jetbrains.annotations.NotNull;


public interface IParadoxItemStack {

    /**
     * Returns this stack's item count as an int
     * @author repletsin5
     * @since API 1.0.0-Alpha
     */
    public int getItemCount();

    /**
     * Sets this stack's item count via an int
     * @author repletsin5
     * @since API 1.0.0-Alpha
     */
    public void setItemCount(@NotNull int value);

    /**
     * Returns the item associated with this item stack
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxItem
     */
    public IParadoxItem getItem();

}

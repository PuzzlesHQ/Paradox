package com.github.puzzle.paradox.api.impl.item;

import com.github.puzzle.paradox.api.interfaces.item.IParadoxItem;
import com.github.puzzle.paradox.api.interfaces.item.IParadoxItemStack;
import finalforeach.cosmicreach.items.ItemStack;
import org.jetbrains.annotations.NotNull;


public class ParadoxItemStack implements IParadoxItemStack {

    ItemStack stack;
    IParadoxItem item;

    public ParadoxItemStack(ItemStack stack){
        this.stack = stack;
        this.item = new ParadoxItem(stack.getItem());
    }



    /**
     * Returns this stack's item count as an int
     * @author repletsin5
     * @since API 1.0.0-Alpha
     */
    public int getItemCount(){
       return this.stack.amount;
    }

    /**
     * Sets this stack's item count via an int
     * @author repletsin5
     * @since API 1.0.0-Alpha
     */
    public void setItemCount(@NotNull int value){
        this.stack.amount = value;
    }

    /**
     * Returns the item associated with this item stack
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see IParadoxItem
     */
    public IParadoxItem getItem(){
        return item;
    }

    /**
     * Avoid using this. Returns Cosmic Reach's internal item stack class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ItemStack
     */
    public ItemStack getInternalItemStack(){
        return stack;
    }
}

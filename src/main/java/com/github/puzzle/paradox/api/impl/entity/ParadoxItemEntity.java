package com.github.puzzle.paradox.api.impl.entity;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxItemEntity;
import com.github.puzzle.paradox.api.interfaces.item.IParadoxItemStack;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.entities.ItemEntity;

public class ParadoxItemEntity extends ParadoxEntity implements IParadoxItemEntity  {
    ItemEntity entity;

    public ParadoxItemEntity(ItemEntity entity) {
        super(entity);
    }

    public IParadoxItemStack getItemStack(){
        return ClassConverter.convertClass((entity).itemStack);
    }


    /**
     * Avoid using this. Returns Cosmic Reach's internal item entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ItemEntity
     */
    public ItemEntity getInternalItemEntity(){
        return entity;
    }
}

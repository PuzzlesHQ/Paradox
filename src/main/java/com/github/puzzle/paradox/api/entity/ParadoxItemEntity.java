package com.github.puzzle.paradox.api.entity;

import com.github.puzzle.paradox.api.item.ParadoxItemStack;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.entities.ItemEntity;

public class ParadoxItemEntity extends ParadoxEntity {
    public ParadoxItemEntity(ItemEntity entity) {
        super(entity);
    }

    public ParadoxItemStack getItemStack(){
        return ClassConverter.convertClass(((ItemEntity)entity).itemStack);
    }


    /**
     * Avoid using this. Returns Cosmic Reach's internal item entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ItemEntity
     */
    public ItemEntity getInternalItemEntity(){
        return (ItemEntity) entity;
    }
}

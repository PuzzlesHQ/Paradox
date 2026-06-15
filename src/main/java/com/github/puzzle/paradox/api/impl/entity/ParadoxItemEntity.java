package com.github.puzzle.paradox.api.impl.entity;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxItemEntity;
import com.github.puzzle.paradox.api.interfaces.item.IParadoxItemStack;
import com.github.puzzle.paradox.core.ClassConverter;
import finalforeach.cosmicreach.entities.ItemGameEntity;

public class ParadoxItemEntity extends ParadoxEntity implements IParadoxItemEntity  {
    ItemGameEntity entity;

    public ParadoxItemEntity(ItemGameEntity entity) {
        super(entity);
    }

    public IParadoxItemStack getItemStack(){
        return ClassConverter.convertClass((entity).itemStack);
    }


    /**
     * Avoid using this. Returns Cosmic Reach's internal item entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see ItemGameEntity
     */
    public ItemGameEntity getInternalItemEntity(){
        return entity;
    }
}

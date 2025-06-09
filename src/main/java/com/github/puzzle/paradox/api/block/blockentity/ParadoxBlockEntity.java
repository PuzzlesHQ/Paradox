package com.github.puzzle.paradox.api.block.blockentity;

import finalforeach.cosmicreach.blockentities.BlockEntity;

public abstract class ParadoxBlockEntity {

    BlockEntity blockEntity;
    public ParadoxBlockEntity(BlockEntity blockEntity){
        this.blockEntity = blockEntity;
    }


    /**
     * Avoid using this. Returns Cosmic Reach's internal block entity class
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see BlockEntity
     */
    public BlockEntity getInternalBlockEntity() {
        return blockEntity;
    }
}

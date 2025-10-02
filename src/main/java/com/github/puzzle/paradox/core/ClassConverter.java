package com.github.puzzle.paradox.core;

import com.github.puzzle.paradox.api.ParadoxNetworkIdentity;
import com.github.puzzle.paradox.api.block.blockentity.ParadoxBlockEntity;
import com.github.puzzle.paradox.api.entity.*;
import com.github.puzzle.paradox.api.item.ParadoxItem;
import com.github.puzzle.paradox.api.player.ParadoxAccount;
import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import com.github.puzzle.paradox.api.item.ParadoxItemStack;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.accounts.AccountItch;
import finalforeach.cosmicreach.accounts.AccountOffline;
import finalforeach.cosmicreach.entities.*;
import finalforeach.cosmicreach.entities.mobs.MobDroneTrap;
import finalforeach.cosmicreach.entities.mobs.MobIncinerator;
import finalforeach.cosmicreach.entities.mobs.MobInterceptor;
import finalforeach.cosmicreach.entities.mobs.MobLaserInterceptor;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.entities.player.PlayerEntity;
import finalforeach.cosmicreach.entities.projectiles.EntityProjectileLaser;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
@SuppressWarnings("unchecked")

public class ClassConverter {

    public static final Logger LOGGER = LoggerFactory.getLogger("Paradox | Class Converter");

    public record EntityInfo<E extends ParadoxEntity, C extends finalforeach.cosmicreach.entities.Entity>
            (String id, Class<E> pEntity, Function<C,E> converter)
    {

    }
    public record PlayerInfo<E extends ParadoxPlayer, C,P extends PlayerEntity>
            (Class<E> pEntity, BiFunction<C,P,E> converter)
    {

    }

    public record ClassInfo<E, C>(String className, Class<E> clazz, Function<C,E> converter)
    {

    }
    public record BlockEntityInfo<E extends ParadoxBlockEntity, C extends finalforeach.cosmicreach.blocks.blockentities.BlockEntity>
            (String id, Class<E> clazz, Function<C,E> converter)
    {

    }
    private static final Map<String,EntityInfo<?,?>> ENTITY_CONVERTERS = new HashMap<>();
    private static final Map<String,BlockEntityInfo<?,?>> BLOCK_ENTITY_CLASS_CONVERTERS = new HashMap<>();
    private static final Map<String,ClassInfo<?,?>> OTHER_CLASS_CONVERTERS = new HashMap<>();

    private static void registerEntityConverter(EntityInfo<?,?> info){
        ENTITY_CONVERTERS.put(info.id, info);
    }
    private static void registerBlockEntityConverter(BlockEntityInfo<?,?> info){
        BLOCK_ENTITY_CLASS_CONVERTERS.put(info.id, info);
    }
    private static void registerClassConverter(ClassInfo<?,?> info){
        OTHER_CLASS_CONVERTERS.put(info.className(), info);
    }


    public static<E extends finalforeach.cosmicreach.entities.Entity> ParadoxEntity convertEntity(E toConvert){
       EntityInfo<ParadoxEntity,E> c = (EntityInfo<ParadoxEntity, E>) ENTITY_CONVERTERS.get(toConvert.entityTypeId);
       if(c == null) {
           LOGGER.warn("Can not convert Entity using ParadoxEntity as default, Entity: {}",toConvert.entityTypeId);
           return new ParadoxEntity(toConvert);
       }
      return c.converter().apply(toConvert);
    }
    public static<E extends finalforeach.cosmicreach.blocks.blockentities.BlockEntity> ParadoxBlockEntity convertEntity(E toConvert){
        BlockEntityInfo<ParadoxBlockEntity,E> c = (BlockEntityInfo<ParadoxBlockEntity, E>) BLOCK_ENTITY_CLASS_CONVERTERS.get(toConvert.getBlockEntityId());
        if(c == null)
            throw new RuntimeException("Can not convert this block entity");
        return c.converter().apply(toConvert);
    }

    public static ParadoxPlayer convertPlayer(Player toConvert){
        PlayerInfo<ParadoxPlayer,Player,PlayerEntity> c = new PlayerInfo<>(ParadoxPlayer.class,ParadoxPlayer::new);
        if(c == null)
            throw new RuntimeException("Can not convert player");

        return c.converter().apply(toConvert,(PlayerEntity)toConvert.getEntity());
    }

    public static <E, R> R convertClass(E toConvert) {
        ClassInfo<R,E> c = (ClassInfo<R,E>) OTHER_CLASS_CONVERTERS.get(toConvert.getClass().getName());
        if(c == null) {
            if (toConvert instanceof Entity e) {
                LOGGER.warn("Can not convert Entity using ParadoxEntity as default, Entity: {}",e.entityTypeId);
                return (R) new ParadoxEntity(e);
            }
            throw new RuntimeException("Can not convert class, Not an entity");
        }
        return c.converter().apply(toConvert);
    }

    static {
        registerEntityConverter(new EntityInfo<>(PlayerEntity.ENTITY_TYPE_ID, ParadoxPlayerEntity.class, ParadoxPlayerEntity::new));
        registerEntityConverter(new EntityInfo<>(MobInterceptor.ENTITY_TYPE_ID, ParadoxDroneEntity.class, ParadoxDroneEntity::new));
        registerEntityConverter(new EntityInfo<>(MobDroneTrap.ENTITY_TYPE_ID, ParadoxDroneTrapEntity.class, ParadoxDroneTrapEntity::new));
        registerEntityConverter(new EntityInfo<>(ItemEntity.ENTITY_TYPE_ID, ParadoxItemEntity.class, ParadoxItemEntity::new));
        registerEntityConverter(new EntityInfo<>(MobIncinerator.ENTITY_TYPE_ID, ParadoxIncinerator.class, ParadoxIncinerator::new));
        registerEntityConverter(new EntityInfo<>(MobLaserInterceptor.ENTITY_TYPE_ID, ParadoxLaserDroneEntity.class, ParadoxLaserDroneEntity::new));
        registerEntityConverter(new EntityInfo<>(EntityProjectileLaser.ENTITY_TYPE_ID, ParadoxLaserProjectileEntity.class, ParadoxLaserProjectileEntity::new));



        registerClassConverter(new ClassInfo<>(NetworkIdentity.class.getName(), ParadoxNetworkIdentity.class, ParadoxNetworkIdentity::new));
        registerClassConverter(new ClassInfo<>(ServerIdentity.class.getName(), ParadoxNetworkIdentity.class, ParadoxNetworkIdentity::new));
        registerClassConverter(new ClassInfo<>(ItemStack.class.getName(), ParadoxItemStack.class, ParadoxItemStack::new));
        registerClassConverter(new ClassInfo<>(Item.class.getName(), ParadoxItem.class, ParadoxItem::new));
        registerClassConverter(new ClassInfo<>(Account.class.getName(), ParadoxAccount.class, ParadoxAccount::new));
        registerClassConverter(new ClassInfo<>(AccountItch.class.getName(), ParadoxAccount.class, ParadoxAccount::new));
        registerClassConverter(new ClassInfo<>(AccountOffline.class.getName(), ParadoxAccount.class, ParadoxAccount::new));
//        registerClassConverter(new ClassInfo<>(Player.class.getName(), ParadoxPlayer.class, ParadoxPlayer::new));
    }

}

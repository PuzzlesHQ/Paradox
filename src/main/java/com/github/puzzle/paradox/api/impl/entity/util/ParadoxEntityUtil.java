package com.github.puzzle.paradox.api.impl.entity.util;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.puzzle.paradox.api.Paradox;
import com.github.puzzle.paradox.api.events.EntityEvents;
import com.github.puzzle.paradox.api.impl.entity.*;
import com.github.puzzle.paradox.api.interfaces.IParadoxZone;
import com.github.puzzle.paradox.api.interfaces.entity.IParadoxEntity;
import com.github.puzzle.paradox.api.interfaces.entity.util.IParadoxEntityUtil;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.EntityCreator;
import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.entities.mobs.MobDroneTrap;
import finalforeach.cosmicreach.entities.mobs.MobIncinerator;
import finalforeach.cosmicreach.entities.mobs.MobInterceptor;
import finalforeach.cosmicreach.entities.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParadoxEntityUtil implements IParadoxEntityUtil {

    public ParadoxEntityUtil(){
        classtoidentity.put(ParadoxPlayerEntity.class, PlayerEntity.ENTITY_TYPE_ID);
        classtoidentity.put(ParadoxDroneEntity.class, MobInterceptor.ENTITY_TYPE_ID);
        classtoidentity.put(ParadoxDroneTrapEntity.class, MobDroneTrap.ENTITY_TYPE_ID);
        classtoidentity.put(ParadoxItemEntity.class, ItemEntity.ENTITY_TYPE_ID);
        classtoidentity.put(ParadoxIncinerator.class, MobIncinerator.ENTITY_TYPE_ID);
    }
    final Map<Class<? extends IParadoxEntity>,String> classtoidentity = new HashMap<>();
    public final ObjectMap<UUID, IParadoxEntity> uuidToParadoxEntity = new ObjectMap<>();
    /**
     * Returns a string id associated with the provided entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @see Entity
     * @see ParadoxEntity
     */
    public String getEntityIDByClass(@NotNull Class<? extends IParadoxEntity> e){
        return classtoidentity.get(e);
    }


    /**
     * Returns a spawned instance of the proved paradox entity
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @param eClass
     * @param position
     * @param zone
     * @see IParadoxEntity
     */
    public <E extends IParadoxEntity> E spawnEntity(@NotNull Class<E> eClass, @NotNull Vector3 position, @NotNull IParadoxZone zone) {

        if(classtoidentity.get(eClass) == null)
            return null;
        var spawned = EntityCreator.get(classtoidentity.get(eClass));
        spawned.setPosition(position);
        var pe = spawned.getParadoxEntity();
        if(Paradox.getInstance().getEventBus().post(new EntityEvents.OnEntitySpawn(pe)).isCanceled())
            return null;
        zone.addEntity(pe);
        return (E) pe;
    }
    /**
     * @author repletsin5
     * @since API 1.0.0-Alpha
     * @param uuid
     * @return ParadoxEntity or null if not found
     * @see IParadoxEntity
     */
    @Override
    public IParadoxEntity getByUUID(@NotNull UUID uuid){
        return uuidToParadoxEntity.get(uuid);
    }

}

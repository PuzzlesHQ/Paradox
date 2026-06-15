package com.github.puzzle.paradox.core.util;

import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.entities.GameEntity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.Zone;
import org.jetbrains.annotations.NotNull;

public class PuzzleEntityUtil {

    public static GameEntity getClosestEntity(@NotNull Zone zone, @NotNull GameEntity sourceEntity) {
        GameEntity closest = null;
        float closestDst = Float.MAX_VALUE;
        float range = sourceEntity.sightRange;

        for (GameEntity entity : zone.getAllEntities()) {
            if (entity != null) {
                if (entity != sourceEntity) {
                    float dst = sourceEntity.position.dst(entity.position);
                    if (!(dst > range)) {
                        if (closest == null) {
                            closest = entity;
                        } else if (closestDst > dst) {
                            closestDst = dst;
                            closest = entity;
                        }
                    }
                }
            }
        }

        return closest;
    }

    public static GameEntity getClosestEntity(@NotNull Zone zone, @NotNull GameEntity sourceEntity, Identifier entityId) {
        GameEntity closest = null;
        float closestDst = Float.MAX_VALUE;
        float range = sourceEntity.sightRange;

        for (GameEntity entity : zone.getAllEntities()) {
            if (entity != null) {
                if (entity != sourceEntity) {
                    if (entity.entityTypeId.equals(entityId.toString())) {
                        float dst = sourceEntity.position.dst(entity.position);
                        if (!(dst > range)) {
                            if (closest == null) {
                                closest = entity;
                            } else if (closestDst > dst) {
                                closestDst = dst;
                                closest = entity;
                            }
                        }
                    }
                }
            }
        }

        return closest;
    }

    public static GameEntity getClosestPlayerToEntity(@NotNull Zone zone, @NotNull GameEntity sourceEntity) {
        Array<Player> players = zone.getPlayers();
        GameEntity closest = null;
        float closestDst = Float.MAX_VALUE;
        float range = sourceEntity.sightRange;

        for (Player p : players) {
            GameEntity pe = p.getEntity();
            if (pe != null) {
                float dst = sourceEntity.position.dst(pe.position);
                if (!(dst > range)) {
                    if (closest == null) {
                        closest = pe;
                    } else if (closestDst > dst) {
                        closestDst = dst;
                        closest = pe;
                    }
                }
            }
        }

        return closest;
    }
}

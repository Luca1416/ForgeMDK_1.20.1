package net.superlucamon.luero.test;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Predicate;

public class RaycastUtils {
    public static Entity raycastEntity(Player source, float delta, float maxDist) {
        Vec3 pos = source.getEyePosition(delta);
        Vec3 rot = source.getViewVector(1.0f);
        Vec3 end = pos.add(rot.x * maxDist, rot.y * maxDist, rot.z * maxDist);
        AABB sourceBox = source.getBoundingBox().expandTowards(rot.scale(maxDist)).inflate(2.5, 2.5, 2.5);
        EntityHitResult entityHitResult = raycastEntity(
                source,
                pos,
                end,
                sourceBox,
                (entity) -> !entity.isSpectator() && entity.isPickable(),
                maxDist * maxDist
        );
        return entityHitResult != null ? entityHitResult.getEntity() : null;
    }

    public static Optional<Entity> raycastEntity(Entity entity, int distance, Predicate<Entity> predicate) {
        Vec3 vec3d = entity.getEyePosition();
        Vec3 vec3d2 = entity.getViewVector(1.0f).scale(distance);
        Vec3 vec3d3 = vec3d.add(vec3d2);
        AABB box = entity.getBoundingBox().expandTowards(vec3d2).inflate(1.0);
        double maxDistSquared = distance * distance;
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entity, vec3d, vec3d3, box, predicate, maxDistSquared);

        if (entityHitResult == null) {
            return Optional.empty();
        } else {
            double squaredDistance = vec3d.distanceToSqr(entityHitResult.getLocation());
            return squaredDistance > maxDistSquared ? Optional.empty() : Optional.of(entityHitResult.getEntity());
        }
    }

    public static EntityHitResult raycastEntity(Player source, Vec3 pos, Vec3 end, AABB box, Predicate<Entity> predicate, double maxDistSquared) {
        double nearestDistance = maxDistSquared;
        Entity target = null;
        Vec3 targetPos = null;

        for (Entity entity : source.level().getEntities(source, box, predicate)) {
            AABB entityBox = entity.getBoundingBox().inflate(entity.getPickRadius());
            Optional<Vec3> optionalPos = entityBox.clip(pos, end);

            if (entityBox.contains(pos)) {
                if (nearestDistance >= 0.0) {
                    target = entity;
                    targetPos = optionalPos.orElse(pos);
                    nearestDistance = 0.0;
                }
            } else if (optionalPos.isPresent()) {
                Vec3 entityPos = optionalPos.get();
                double squaredDistance = pos.distanceToSqr(entityPos);

                if (squaredDistance < nearestDistance || nearestDistance == 0.0) {
                    if (entity.getRootVehicle() == source.getRootVehicle() && nearestDistance == 0.0) {
                        target = entity;
                        targetPos = entityPos;
                    } else {
                        target = entity;
                        targetPos = entityPos;
                        nearestDistance = squaredDistance;
                    }
                }
            }
        }
        return target != null ? new EntityHitResult(target, targetPos) : null;
    }
}



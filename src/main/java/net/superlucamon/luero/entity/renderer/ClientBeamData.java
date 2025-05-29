package net.superlucamon.luero.entity.renderer;

import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientBeamData {
    public static class BeamInfo {
        private Vec3 lastOrigin;
        private Vec3 currentOrigin;
        private Vec3 lastDirection;
        private Vec3 currentDirection;
        private boolean initialized = false;

        public BeamInfo(Vec3 origin, Vec3 direction) {
            this.lastOrigin = origin;
            this.currentOrigin = origin;
            this.lastDirection = direction;
            this.currentDirection = direction;
            this.initialized = true;
        }

        public void update(Vec3 newOrigin, Vec3 newDirection) {
            if (!initialized) {
                this.lastOrigin = newOrigin;
                this.currentOrigin = newOrigin;
                this.lastDirection = newDirection;
                this.currentDirection = newDirection;
                this.initialized = true;
            } else {
                this.lastOrigin = this.currentOrigin;
                this.currentOrigin = newOrigin;
                this.lastDirection = this.currentDirection;
                this.currentDirection = newDirection;
            }
        }


        public Vec3 getInterpolatedOrigin(float partialTicks) {
            return interpolate(lastOrigin, currentOrigin, partialTicks);
        }

        public Vec3 getInterpolatedDirection(float partialTicks) {
            return interpolate(lastDirection, currentDirection, partialTicks).normalize();
        }


        private Vec3 interpolate(Vec3 from, Vec3 to, float partialTicks) {
            double x = from.x + (to.x - from.x) * partialTicks;
            double y = from.y + (to.y - from.y) * partialTicks;
            double z = from.z + (to.z - from.z) * partialTicks;
            return new Vec3(x, y, z);
        }
    }

    private static final Map<UUID, BeamInfo> beamMap = new HashMap<>();

    public static void updateBeam(UUID uuid, Vec3 origin, Vec3 direction) {
        beamMap.compute(uuid, (id, old) -> {
            if (old == null) return new BeamInfo(origin, direction);
            old.update(origin, direction);
            return old;
        });
    }

    public static BeamInfo getRenderInfo(UUID uuid) {
        return beamMap.get(uuid);
    }

    public static Map<UUID, BeamInfo> getAll() {
        return beamMap;
    }

    public static void set(UUID playerId, Vec3 origin, Vec3 direction) {
        updateBeam(playerId, origin, direction);
    }

    public static void remove(UUID playerId) {
        beamMap.remove(playerId);
    }

    public static boolean isFiring(UUID uuid) {
        return beamMap.containsKey(uuid);
    }

    public static void stopFiring(UUID uuid) {
        beamMap.remove(uuid);
    }
}

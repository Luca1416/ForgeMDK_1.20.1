package net.superlucamon.luero.heros.ironman.otherstuff;

import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BeamTracking {
    public static final Map<UUID, Long> ACTIVE_BEAMS = new ConcurrentHashMap<>();

    public static void startBeam(Player player) {
        ACTIVE_BEAMS.put(player.getUUID(), System.currentTimeMillis());
    }

    public static boolean isBeaming(Player player) {
        return ACTIVE_BEAMS.containsKey(player.getUUID());
    }

    public static void stopBeam(Player player) {
        ACTIVE_BEAMS.remove(player.getUUID());
    }
}

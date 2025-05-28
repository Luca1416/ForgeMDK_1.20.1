package net.superlucamon.luero.entity.renderer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClientBeamData {
    private static final Set<UUID> firingPlayers = new HashSet<>();

    public static void startFiring(UUID uuid) {
        firingPlayers.add(uuid);
    }

    public static void stopFiring(UUID uuid) {
        firingPlayers.remove(uuid);
    }

    public static boolean isFiring(UUID uuid) {
        return firingPlayers.contains(uuid);
    }
}

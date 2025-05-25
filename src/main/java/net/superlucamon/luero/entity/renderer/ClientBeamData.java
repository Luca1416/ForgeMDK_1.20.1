package net.superlucamon.luero.entity.renderer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClientBeamData {
    private static final Set<UUID> activeBeams = new HashSet<>();

    public static void startBeamFor(UUID uuid) {
        activeBeams.add(uuid);
    }

    public static void stopBeamFor(UUID uuid) {
        activeBeams.remove(uuid);
    }

    public static boolean isFiring(UUID uuid) {
        return activeBeams.contains(uuid);
    }

    public static Set<UUID> getAllFiringPlayers() {
        return activeBeams;
    }
}

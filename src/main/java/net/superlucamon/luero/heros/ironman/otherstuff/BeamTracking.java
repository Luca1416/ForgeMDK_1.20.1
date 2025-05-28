package net.superlucamon.luero.heros.ironman.otherstuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.networking.packet.StartBeamPacket;
import net.superlucamon.luero.networking.packet.StopBeamPacket;
import net.superlucamon.luero.register.ModSounds;

import java.util.*;

public class BeamTracking {
    private static final Set<UUID> firingPlayers = new HashSet<>();

    public static void startBeam(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (firingPlayers.add(uuid)) {
            ModPackets.sendToTracking(player, new StartBeamPacket(uuid));
        }
    }

    public static void stopBeam(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (firingPlayers.remove(uuid)) {
            ModPackets.sendToTracking(player, new StopBeamPacket(uuid));
        }
    }

    public static boolean isFiring(UUID uuid) {
        return firingPlayers.contains(uuid);
    }
    public static void tickBeam(ServerPlayer player) {
        if (!player.level().isClientSide) {
            Level level = player.level();
            Vec3 eyePos = player.getEyePosition(1.0f);
            Vec3 look = player.getLookAngle();
            Vec3 beamPos = eyePos.subtract(0, 0.0f, 0);
            Vec3 from = beamPos;
            Vec3 to = from.add(look.scale(15));

            // Play beam sound once when it starts
            if (player.tickCount % 20 == 0) {
                level.playSeededSound(null, beamPos.x, beamPos.y, beamPos.z,
                        ModSounds.UNI_BEAM.get(), SoundSource.PLAYERS, 1.0f, 1.0f, 0);
            }

            // ENTITY HIT
            AABB aabb = new AABB(from, to).inflate(0.05);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, player, from, to, aabb, e -> e != player && e.isAlive());
            if (entityHit != null && entityHit.getEntity() != null) {
                entityHit.getEntity().hurt(level.damageSources().magic(), 1.0f);
            }

            // BLOCK HIT
            HitResult hitResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if (hitResult instanceof BlockHitResult blockHit) {
                BlockPos hitPos = blockHit.getBlockPos();
                BlockState state = level.getBlockState(hitPos);

                // Break block logic (with cooldown)
                beamTickCounter.putIfAbsent(player.getUUID(), 0);
                if (canBreak(state)) {
                    int ticksOnBlock = beamTickCounter.get(player.getUUID());
                    if (ticksOnBlock >= 30) {
                        level.destroyBlock(hitPos, false);
                        ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION,
                                hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5,
                                1, 0, 0, 0, 0);
                        beamTickCounter.put(player.getUUID(), 0);
                    } else {
                        beamTickCounter.put(player.getUUID(), ticksOnBlock + 1);
                    }
                }

                // Fire placement
                if (level.getBlockState(hitPos.above()).isAir()
                        && state.isSolid() && level.random.nextFloat() < 0.08F) {
                    level.setBlockAndUpdate(hitPos.above(), Blocks.FIRE.defaultBlockState());
                }
            }
        }
    }

    private static final Map<UUID, Integer> beamTickCounter = new HashMap<>();

    private static boolean canBreak(BlockState state) {
        return !state.isAir() &&
                !state.is(BlockTags.NEEDS_DIAMOND_TOOL) &&
                !state.is(BlockTags.NEEDS_IRON_TOOL) &&
                !state.is(BlockTags.NEEDS_STONE_TOOL);
    }
    public static void serverTick(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            if (isFiring(player.getUUID())) {
                tickBeam(player);
            }
        }
    }

}

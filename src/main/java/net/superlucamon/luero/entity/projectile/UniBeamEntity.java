package net.superlucamon.luero.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.superlucamon.luero.register.ModSounds;

import java.util.Optional;
import java.util.UUID;

public class UniBeamEntity extends Entity {
    private int ticksExisted = 0;
    private Player owner;

    private BlockPos lastHitBlock = null;
    private int ticksOnSameBlock = 0;
    private final int BREAK_TICKS = 30; // Number of ticks needed to break a block
    private boolean enableFirePlacement = true;
    private float beamProgress = 0f;
    private boolean firstPersonView = false;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(UniBeamEntity.class, EntityDataSerializers.OPTIONAL_UUID);




    public UniBeamEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setPlayer(Player player) {
        this.owner = player;
        this.entityData.set(OWNER_UUID, Optional.of(player.getUUID()));
    }
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }


    public float getBeamProgress() {
        return beamProgress;
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        ticksExisted++;
        if (beamProgress < 1.0f) {
            beamProgress += 0.08f;
        }

        if (ticksExisted > 160) {
            this.discard();
            beamProgress = 0;
            return;
        }
        if (owner != null) {
            // Follow player's position and look direction
            if (!level().isClientSide) {
                level().playSeededSound(null, getX(), getY(), getZ(),
                        ModSounds.UNI_BEAM.get(),
                        SoundSource.PLAYERS, 1.0f, 1.0f, 0);
            }
            Vec3 eyePos = owner.getEyePosition(1.0F);
            Vec3 look = owner.getLookAngle();

            Vec3 beamPos = eyePos.subtract(0, 0.0f, 0);
                var mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.player != null &&
                        mc.player.getUUID().equals(this.owner.getUUID()) &&
                        mc.options.getCameraType().isFirstPerson()) {

                    // Exact same check as the renderer uses
                    Vec3 backOffset = owner.getLookAngle().scale(0.5);
                    beamPos = beamPos.subtract(backOffset);
                }
            this.setPos(beamPos.x, beamPos.y - 0.5f, beamPos.z);

            this.setYRot(owner.getYRot());
            this.setXRot(owner.getXRot());

            Vec3 from = eyePos;
            Vec3 to = from.add(look.scale(15));

            // Entity hit detection
            double minX = Math.min(from.x, to.x);
            double minY = Math.min(from.y, to.y);
            double minZ = Math.min(from.z, to.z);
            double maxX = Math.max(from.x, to.x);
            double maxY = Math.max(from.y, to.y);
            double maxZ = Math.max(from.z, to.z);
            AABB aabb = new AABB(minX, minY, minZ, maxX, maxY, maxZ).inflate(0.05);

            drawAABB(level(), aabb);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level(), this, from, to, aabb, e -> e != owner && e.isAlive());

            if (entityHit != null && entityHit.getEntity() != null) {
                entityHit.getEntity().hurt(level().damageSources().magic(), 1.0f);
            }
            // Block ray trace
            HitResult hitResult = this.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

            if (hitResult instanceof BlockHitResult blockHit) {
                BlockPos hitPos = blockHit.getBlockPos();
                BlockState state = level().getBlockState(hitPos);

                if (canBreak(state)) {
                    if (lastHitBlock != null && lastHitBlock.equals(hitPos)) {
                        ticksOnSameBlock++;
                        if (ticksOnSameBlock >= BREAK_TICKS) {
                            level().destroyBlock(hitPos, false);
                            if (!level().isClientSide) {
                                ((ServerLevel) level()).sendParticles(ParticleTypes.EXPLOSION, hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5, 1, 0, 0, 0, 0);
                            }
                            ticksOnSameBlock = 0;
                        }
                    } else {
                        lastHitBlock = hitPos;
                        ticksOnSameBlock = 0;
                    }
                }

                if (enableFirePlacement &&
                        level().getBlockState(hitPos.above()).isAir() &&
                        state.isSolid() &&
                        level().random.nextFloat() < 0.08F) {

                    level().setBlockAndUpdate(hitPos.above(), Blocks.FIRE.defaultBlockState());
                }

            }

            // Particle trail
            if (level().isClientSide) {
                int parts = 100;
                for (int i = 0; i < parts; i++) {
                    double t = i / (double) parts;
                    Vec3 point = from.lerp(to, t);
                    level().addParticle(ParticleTypes.END_ROD, point.x, point.y, point.z, 0, 0, 0);
                }
            }
        }}

        private boolean canBreak(BlockState state) {
            return !state.isAir() &&
                    !state.is(BlockTags.NEEDS_DIAMOND_TOOL) &&
                    !state.is(BlockTags.NEEDS_IRON_TOOL) &&
                    !state.is(BlockTags.NEEDS_STONE_TOOL);
        }
    public static void drawAABB(Level level, AABB box) {
        double minX = box.minX;
        double minY = box.minY;
        double minZ = box.minZ;
        double maxX = box.maxX;
        double maxY = box.maxY;
        double maxZ = box.maxZ;

        // bottom edges
        drawLine(level, minX, minY, minZ, maxX, minY, minZ);
        drawLine(level, maxX, minY, minZ, maxX, minY, maxZ);
        drawLine(level, maxX, minY, maxZ, minX, minY, maxZ);
        drawLine(level, minX, minY, maxZ, minX, minY, minZ);

        // top edges
        drawLine(level, minX, maxY, minZ, maxX, maxY, minZ);
        drawLine(level, maxX, maxY, minZ, maxX, maxY, maxZ);
        drawLine(level, maxX, maxY, maxZ, minX, maxY, maxZ);
        drawLine(level, minX, maxY, maxZ, minX, maxY, minZ);

        // vertical edges
        drawLine(level, minX, minY, minZ, minX, maxY, minZ);
        drawLine(level, maxX, minY, minZ, maxX, maxY, minZ);
        drawLine(level, maxX, minY, maxZ, maxX, maxY, maxZ);
        drawLine(level, minX, minY, maxZ, minX, maxY, maxZ);
    }

    private static void drawLine(Level level, double x1, double y1, double z1, double x2, double y2, double z2) {
        int points = 20; // more = smoother line
        for (int i = 0; i <= points; i++) {
            double t = i / (double) points;
            double x = x1 + (x2 - x1) * t;
            double y = y1 + (y2 - y1) * t;
            double z = z1 + (z2 - z1) * t;
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }
    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}

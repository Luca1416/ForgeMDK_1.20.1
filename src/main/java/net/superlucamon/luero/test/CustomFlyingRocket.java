package net.superlucamon.luero.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.superlucamon.luero.block.ModTags;

import java.util.EnumSet;

public class CustomFlyingRocket extends FlyingMob {
    private static final EntityDataAccessor<Float> TARGET_YAW =
            SynchedEntityData.defineId(CustomFlyingRocket.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_PITCH =
            SynchedEntityData.defineId(CustomFlyingRocket.class, EntityDataSerializers.FLOAT);
    private Player owner;
    private LivingEntity targetEntity;
    private Vec3 targetPos;
    private Vec3 missilePos;
    private Vec3 direction;
    private Vec3 motion;
    private BlockPos blockPosEntity;
    private int cooldown = 60;
    private float targetYaw;

    public CustomFlyingRocket(EntityType<? extends FlyingMob> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 180, true);
        this.setNoGravity(true);
    }

    @Override
    public void onAddedToWorld() {

       /* if (owner != null){
            this.setPos(owner.position());
        }
        */

    }

    public static AttributeSupplier.Builder createMissileAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.02)
                .add(Attributes.FLYING_SPEED, 0.02);

    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_YAW, 0.0f);
        this.entityData.define(TARGET_PITCH, 0.0f);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FollowTargetGoal(this, 0.02));
    }

    private boolean canHit(Entity entity) {
        return true;
    }
    public float getTargetYaw() {
        return this.entityData.get(TARGET_YAW);
    }

    public void setTargetYaw(float yaw) {
        this.entityData.set(TARGET_YAW, yaw);
    }

    public float getTargetPitch() {
        return this.entityData.get(TARGET_PITCH);
    }

    public void setTargetPitch(float pitch) {
        this.entityData.set(TARGET_PITCH, pitch);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }
            if (isFlyingMobTouchingBlock(this) && this.tickCount >= cooldown) {
                this.explodeAndDiscard();
            }
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                this.explodeAndDiscard();
            }
        }
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.1, 0.0, 0.1);
        }
    }

    private void explodeAndDiscard() {
        this.level().explode(
                owner,
                this.getX(),
                this.getY(),
                this.getZ(),
                getRandom().nextIntBetweenInclusive(1, 2),
                Level.ExplosionInteraction.MOB
        );
        this.discard();
    }

    public static boolean isFlyingMobTouchingBlock(Mob mob) {
        AABB boundingBox = mob.getBoundingBox();

        for (BlockPos pos : BlockPos.betweenClosed(
                new BlockPos((int) boundingBox.minX, (int) boundingBox.minY, (int) boundingBox.minZ),
                new BlockPos((int) boundingBox.maxX, (int) boundingBox.maxY, (int) boundingBox.maxZ))) {

            BlockState blockState = mob.level().getBlockState(pos);

            if (!blockState.isAir() && !blockState.is(ModTags.Blocks.IGNORE_BLOCKS)) {
                return true;
            }
        }
        return false;
    }

    private void onCollision(HitResult hitResult) {
        if (hitResult instanceof EntityHitResult && ((EntityHitResult) hitResult).getEntity() != owner && ((EntityHitResult) hitResult).getEntity() instanceof LivingEntity && ((EntityHitResult) hitResult).getEntity() != this) {
            explodeAndDiscard();
        }
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, world);
        navigation.setCanOpenDoors(true);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public LivingEntity getTarget() {
        return this.targetEntity;
    }

    public void setTargetEntity(LivingEntity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distanceSquared) {
        return true;
    }


    @Override
    public void checkDespawn() {
        if (!this.isAlive() || targetEntity == null) {
            this.discard();
        }
    }

    private class FollowTargetGoal extends Goal {
        private CustomFlyingRocket missile;
        private final double speed;
        private LivingEntity target;
        private int delay;
        private float roll = 0.0f;


        public FollowTargetGoal(CustomFlyingRocket missile, double speed) {
            this.missile = missile;
            this.speed = speed;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            target = this.missile.getTarget();
            return target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && target.isAlive() && missile.distanceToSqr(target) >= 9.0 && missile.distanceToSqr(target) <= 256.0;
        }

        @Override
        public void tick() {
            if (target != null) {
                blockPosEntity = target.blockPosition();
                Vec3 targetPos = target.getEyePosition();
                Vec3 missilePos = missile.position();
                Vec3 direction = targetPos.subtract(missilePos).normalize();
                Vec3 motion = direction.scale(speed);

                missile.setDeltaMovement(motion);
                //updateRotation(direction);
                //missile.lookAt(target, 30.0F, 30.0F);
                float dX = (float) target.getX() -   (float) missile.getX();
                float dY =  (float) target.getY() -  (float)missile.getY();
                float dZ =  (float)target.getZ() -  (float)missile.getZ();
                float yaw =  (float) Math.atan2(dZ, dX);
                // Calculate pitch (rotation around X-axis)
                float pitch = (float) Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + (float) Math.PI;


                // Save these values so that the renderer can access them on the client side
                missile.setTargetYaw(yaw);
                missile.setTargetPitch(pitch);



            } else {
                missile.setPos(blockPosEntity.getX(), blockPosEntity.getY(), blockPosEntity.getZ());
            }

        }
        private void updateRotation(Vec3 direction) {
            // Calculate yaw (horizontal angle)
            float yaw = (float) (Math.atan2(direction.z, direction.x) * (180 / Math.PI)) - 90;

            // Calculate pitch (vertical angle)
            float pitch = (float) -(Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)) * (180 / Math.PI));

            // Apply rotations — yaw controls facing direction, pitch controls tilt
            missile.setYRot(lerpRotation(missile.getYRot(), yaw, 0.2f));
            missile.setXRot(lerpRotation(missile.getXRot(), pitch, 0.2f));

            // Ensure rotations are correctly updated
            missile.setRot(missile.getYRot(), missile.getXRot());

            // Save previous rotations (for smooth interpolation)
            missile.yHeadRot = missile.getYRot();
            missile.xRotO = missile.getXRot();
        }



        private float lerpRotation(float current, float target, float factor) {
            float delta = target - current;
            delta = (delta + 180) % 360 - 180; // Ensure shortest path for angles
            return current + delta * factor;
        }



    }

}


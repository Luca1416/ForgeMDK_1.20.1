package net.superlucamon.luero.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.superlucamon.luero.block.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class CustomFlyingRocket extends FlyingMob {

    private Player owner;
    private LivingEntity targetEntity;
    private Vec3 targetPos;
    private Vec3 missilePos;
    private Vec3 direction;
    private Vec3 motion;
    private BlockPos blockPosEntity;
    private int cooldown = 60;
    private PathNavigation navi = createNavigation(level());

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
                .add(Attributes.MOVEMENT_SPEED, 0.6)
                .add(Attributes.FLYING_SPEED, 0.6);

    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FollowTargetGoal(this, 0.6));
    }

    private boolean canHit(Entity entity) {
        return true;
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
                getRandom().nextIntBetweenInclusive(1,2),
                Level.ExplosionInteraction.MOB
        );
        this.discard();
    }
    public static boolean isFlyingMobTouchingBlock(Mob mob) {
        AABB boundingBox = mob.getBoundingBox();

        for (BlockPos pos : BlockPos.betweenClosed(
                new BlockPos((int)boundingBox.minX,(int) boundingBox.minY,(int) boundingBox.minZ),
                new BlockPos((int)boundingBox.maxX,(int)boundingBox.maxY,(int) boundingBox.maxZ))) {

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

    @Nullable
    @Override
    public Entity getControlledVehicle() {
        return super.getControlledVehicle();
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

    public ItemStack getStack() {
        return new ItemStack(Items.FIREWORK_ROCKET);
    }

    @Override
    public void baseTick() {
        if (getTarget() != null) {
           // this.goalSelector.tick();
        }
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
        public void start() {
            this.delay = 0;
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (target != null) {

                // System.out.println(target + " - " + missile);
                blockPosEntity = target.blockPosition();
                targetPos = target.getEyePosition();
                missilePos = missile.position();
                direction = targetPos.subtract(missilePos).normalize();
                motion = direction.scale(speed);
                missile.lerpMotion(motion.x, motion.y, motion.z);
                missile.noPhysics = true;

                missile.lookAt(targetEntity, 5f, 5f);

                //missile.getLookControl().setLookAt(missile, 30, 30);
                //missile.getLookControl().setLookAt(target, 30, 30);
                //missile.lookAt(target, 5, 5);
                //missile.setXRot(-185f);
                //missile.setYRot(-180f);

/*
                Vec3 velocity = missile.getDeltaMovement(); // Get the entity's velocity vector
                Vec3 direction = velocity.normalize();
                double dx = direction.x;
                double dz = direction.z;
                float yaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90; // Convert to degrees
                double dy = direction.y;
                float distance = (float) Math.sqrt(dx * dx + dz * dz);
                float pitch = (float) -(Math.atan2(dy, distance) * (180 / Math.PI)); // Convert to degrees
                missile.setYRot(yaw);
                missile.setXRot(pitch);
*/

                missile.hasImpulse = true;
                missile.markHurt();
            }
            else {
                missile.setPos(blockPosEntity.getX(), blockPosEntity.getY(), blockPosEntity.getZ());
            }
        }

        @Override
        protected int adjustedTickDelay(int pAdjustment) {
            pAdjustment *= 0.1F;
            return super.adjustedTickDelay(pAdjustment);
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}

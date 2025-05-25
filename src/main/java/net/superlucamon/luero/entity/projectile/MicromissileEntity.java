package net.superlucamon.luero.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MicromissileEntity extends ThrowableProjectile {
    private static final double SEEK_RANGE = 24;

    private Entity targetEntity = null;
    private float maxVelocitySq = 0.02f;
    private float accel = 1.05f; // straight line acceleration
    private float turnSpeed = 0.1f;
    private float explosionPower = 2f;
    private boolean outOfFuel = false;


    public MicromissileEntity(EntityType<MicromissileEntity> type, Level worldIn) {
        super(type, worldIn);
    }


    @Override
    protected void defineSynchedData() {
    }
    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount == 1) {
            if (getCommandSenderWorld().isClientSide) {
                getCommandSenderWorld().playLocalSound(getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0f, 0.8f, true);
            }
        }

        if (!outOfFuel) {
            // undo default slowdown of projectiles applied in super.tick()
            if (this.isInWater()) {
                setDeltaMovement(getDeltaMovement().scale(1.25));
            } else {
                setDeltaMovement(getDeltaMovement().scale(1 / 0.99));
            }

            if (targetEntity != null) {
                // turn toward the target
                Vec3 diff = targetEntity.position().add(0, targetEntity.getEyeHeight(), 0).subtract(position()).normalize().scale(turnSpeed);
                setDeltaMovement(getDeltaMovement().add(diff));
            }

            // accelerate up to max velocity but cap there
            double velSq = getDeltaMovement().lengthSqr();
            double mul = velSq > maxVelocitySq ? maxVelocitySq / velSq : accel;
            setDeltaMovement(getDeltaMovement().scale(mul));

            if (getCommandSenderWorld().isClientSide && getCommandSenderWorld().random.nextBoolean()) {
                Vec3 m = getDeltaMovement();
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), -m.x/2, -m.y/2, -m.z/2);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (tickCount > 5 && !getCommandSenderWorld().isClientSide && isAlive()) {
            explode(result instanceof EntityHitResult ? ((EntityHitResult) result).getEntity() : null);
        }
    }

    private void explode(Entity e) {
        discard();

        Level.ExplosionInteraction mode = Level.ExplosionInteraction.TNT;
        boolean fire = true;
        float radius = 4.0f;

        double x, y, z;
        if (e == null) {
            x = getX();
            y = getY();
            z = getZ();
        } else {
            // make the explosion closer to the target entity (a fast projectile's position could be a little distance away)
            x = Mth.lerp(0.25f, e.getX(), getX());
            y = Mth.lerp(0.25f, e.getY(), getY());
            z = Mth.lerp(0.25f, e.getZ(), getZ());
        }
        getCommandSenderWorld().explode(this, x, y, z, radius, fire, mode);
    }

    @Override
    public void shootFromRotation(Entity entityThrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        float x = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
        float y = -Mth.sin(pitch * 0.017453292F);
        float z = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
        this.shoot(x, y, z, velocity, 0f);
        setDeltaMovement(getDeltaMovement().add(entityThrower.getDeltaMovement().x, 0, entityThrower.getDeltaMovement().z));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        double f = Math.sqrt(x * x + y * y + z * z);
        x = x / f * velocity;
        y = y / f * velocity;
        z = z / f * velocity;
        setDeltaMovement(x, y, z);

        float f1 = Mth.sqrt((float) (x * x + z * z));
        this.setYRot((float)(Mth.atan2(x, z) * (180D / Math.PI)));
        this.setXRot((float)(Mth.atan2(y, f1) * (180D / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public boolean isNoGravity() {
        return !outOfFuel;
    }
}
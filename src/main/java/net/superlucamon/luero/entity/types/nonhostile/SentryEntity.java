package net.superlucamon.luero.entity.types.nonhostile;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.damagesources.CustomDamageSources;
import net.superlucamon.luero.entity.projectile.MicromissileEntity;
import net.superlucamon.luero.util.UtilMethods;
import org.joml.Random;

import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.superlucamon.luero.test.CustomEntityRegister.CUSTOM_SMALL_FIREBALL;

@Mod.EventBusSubscriber(modid = "heromod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SentryEntity extends PathfinderMob {
    public boolean isReturing;
    public static Entity mSentry;
    public static ModelPart rightArm;
    public static boolean readyForTransform;
    public static boolean transformingToPlayer;
    public static boolean isAttacking;
    public AnimationState sonicBoomAnimationState = new AnimationState();

    public SentryEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.registerGoals();
    }

    @Override
    public void onAddedToWorld() {
        this.noPhysics = false;
        this.readyForTransform = false;
        this.transformingToPlayer = false;
        mSentry = this;
    }

    @Override
    public void checkDespawn() {
        if (!this.isAlive()){
            this.readyForTransform = false;
        }
    }


    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        super.updateWalkAnimation(pPartialTick);
        this.hasImpulse = true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }


    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Monster.class, 20.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new ShootMissileGoal(this, 30F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, false));
    }

    public static AttributeSupplier.Builder createSentryAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.ATTACK_DAMAGE, 1.5)
                .add(Attributes.FOLLOW_RANGE, 25f)
                .add(Attributes.ARMOR, 12.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1f);

    }





    class ShootMissileGoal extends Goal{
        private SentryEntity sentry;
        private Path path;
        private int cooldownTicks = 40;
        private int thrusterCooodown = 10;
        private int timeSinceArrival = 0;
        private int thrusterShotArrival = 0;
        private float limit = 1;
        private float count = 0;
        private final ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(25);

        public ShootMissileGoal(Mob mob, float range){
            sentry = (SentryEntity) mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
        }
        @Override
        public boolean canUse() {
          return sentry != null && sentry.getTarget() != null;
        }


        @Override
        public void start() {
            if (sentry.getTarget() != null && sentry.getTarget().isAlive()) {
              //  spawnMissle(sentry, sentry.getTarget(), (ServerLevel) sentry.level(), (int) count * 1500, mScheduler);
               // System.out.println(count);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return true; //sentry.getHealth() >= 5.0 && !sentry.navigation.isInProgress();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }


        @Override
        public boolean isInterruptable() {
            return false;
        }





        @Override
        public void tick() {
            super.tick();
            if (getTarget() != null) {
                if (sentry.getSensing().hasLineOfSight(getTarget()) && !isReturing) {
                    path = this.sentry.getNavigation().createPath(getTarget(), 0);
                    sentry.getLookControl().setLookAt(getTarget(), 30.0F, 30.0F);
                    if (distanceTo(getTarget()) >= 8F) {
                        this.sentry.getNavigation().moveTo(path, 1F);
                    }
                    if (distanceTo(getTarget()) < 4F) {
                        isAttacking = true;
                        ++thrusterShotArrival;
                        if (thrusterShotArrival >= thrusterCooodown) {
                            getTarget().hurt(CustomDamageSources.missle(this.sentry), 2.0f);

                            sentry.level().playSound(null, sentry.getX(), sentry.getY(), sentry.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.AMBIENT, 0.8f, (float) UtilMethods.getRandomInRange(0.8F, 1.2F));
                            thrusterShotArrival = 0;
                        }
                    } else isAttacking = false;

                    if (distanceTo(getTarget()) < 8F){
                        path = null;
                        ++timeSinceArrival;
                        if (timeSinceArrival >= cooldownTicks) {
                            sentry.getNavigation().stop();
                            spawnMissle(this.sentry, getTarget(), (ServerLevel) this.sentry.level(), 1500, mScheduler);
                            getTarget().setLastHurtByMob(this.sentry);
                            timeSinceArrival = 0;
                            System.out.println("yo");
                        }
                    }
                }
            }
        }
    }
    private void spawnMissle(Entity sentry, LivingEntity target, ServerLevel level, int delay, ScheduledExecutorService scheduler){
        SpawnMissileTask lTask = new SpawnMissileTask(sentry, target, level);
        scheduler.schedule(lTask, delay, TimeUnit.MILLISECONDS);
    }
    private class SpawnMissileTask implements Runnable {
        private final Entity mPlayer;
        private final LivingEntity mLivingEntity;
        private final Level mLevel;

        public SpawnMissileTask(Entity pPlayer, LivingEntity pLivingEntity, ServerLevel pLevel) {
            this.mPlayer = pPlayer;
            this.mLivingEntity = pLivingEntity;
            this.mLevel = pLevel;
        }

        public static double getRandomInRange(double min, double max) {
            Random random = new Random();
            return min + (max - min) * random.nextFloat();
        }
        @Override
        public void run() {
            mLevel.getServer().execute(() -> {
                //if (world instanceof ServerLevel) {
                double x = getRandomInRange(-5, 5);
                double y = getRandomInRange(3, 7);
                double z = getRandomInRange(-5, 5);
                MicromissileEntity lRocket = new MicromissileEntity(CUSTOM_SMALL_FIREBALL.get(), mLevel);
                if (mLivingEntity != null) //add functionality for comparing Entity Class with Missile Class to avoid targeting missiles
                    {
                    lRocket.setTargetEntity(mLivingEntity);
                    if (rightArm != null) {
                        //System.out.println(rightArm.x);
                        lRocket.setPos(rightArm.x, rightArm.y, rightArm.z);
                    }

                    lRocket.setPos(mPlayer.getPosition(1).x(), mPlayer.getEyeY(), mPlayer.getPosition(1).z());


                    //}
                    // Initial position
                    mLevel.addFreshEntity(lRocket);
                    mLevel.playSound(null, mPlayer.getX(), mPlayer.getY(), mPlayer.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 0.8f, (float) getRandomInRange(0.8F, 1.2F));
                }
            });
        }
    }
    @Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public class SentryRenderHandler {
        @SubscribeEvent
        public static void onPreRender(RenderLivingEvent.Pre<?, ?> event) {
            if (event.getEntity() instanceof SentryEntity) {
                LivingEntityRenderer<?, ?> renderer = (LivingEntityRenderer<?, ?>) event.getRenderer();
                HumanoidModel<?> model = (HumanoidModel<?>) renderer.getModel();
                rightArm = model.rightArm;
                // ... do stuff ...
            }
        }
    }

    private static Vec3 getRightHandPosition(Entity player) {
        float f = player.getYRot() * ((float)Math.PI / 180F);
        float f1 = player.getXRot() * ((float)Math.PI / 180F);
        double d0 = player.getX() - (double)(player.getBbWidth() + 1.0F) * 0.5D * Math.sin(f);
        double d1 = player.getEyeY() - (double)player.getBbHeight() * 0.5D;
        double d2 = player.getZ() + (double)(player.getBbWidth() + 1.0F) * 0.5D * Math.cos(f);
        Vec3 rightHandVec = new Vec3(d0, d1, d2);

        // Adjust position slightly based on player's look direction
        double adjustX = -Math.sin(f) * 0.3D;
        double adjustZ = Math.cos(f) * 0.3D;
        return rightHandVec.add(adjustX, -0.1D, adjustZ);
    }
}
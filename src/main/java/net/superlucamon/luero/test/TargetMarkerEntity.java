package net.superlucamon.luero.test;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class TargetMarkerEntity extends Entity {

    private LivingEntity mTargetEntity = null;
    private static int tickChecker;
    private static int mCoolDownForDespawn = 80;
    private int mTickCount = 0;

    public TargetMarkerEntity(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        //this.noPhysics = true;
    }

    public void setTargetEntity(LivingEntity pTargetEntity) {
        mTargetEntity = pTargetEntity;
    }

    @Override
    protected void defineSynchedData() {

    }
/*
    private class PositionUpdateTask implements Runnable {

        private final TargetMarkerEntity entityStickingAbove;
        private final Entity parentEntity;

        public PositionUpdateTask(TargetMarkerEntity entity, Entity parent) {
            this.entityStickingAbove = entity;
            this.parentEntity = parent;
        }

        @Override
        public void run() {
            if (parentEntity != null) {
                double targetX = parentEntity.getX();
                double targetY = parentEntity.getY() + 1.0; // Example offset
                double targetZ = parentEntity.getZ();

                // Simulate heavy computation or complex logic
                // Example: Add some delay to simulate processing
                try {
                    Thread.sleep(100); // Simulate processing time (100 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Update entity position on the main thread using CompletableFuture
                Level world = entityStickingAbove.level();
                CompletableFuture.runAsync(() -> {
                    world.getProfiler().push("entity_position_update");
                    entityStickingAbove.setPos(targetX, targetY, targetZ);
                    world.getProfiler().pop();
                });
            }
        }
    }

    private class ThreadTaskExecutor {
        private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(1);

        public static void submitTask(Runnable pTask) {
            THREAD_POOL.submit(pTask);
        }

        public static void shutdown() {
            THREAD_POOL.shutdown();
        }
    }
*/
    @Override
    public void tick() {
        super.tick();
        //System.out.println("Tick: " + mTickCount++);


        if (mTargetEntity != null) {

            if (!level().isClientSide()) {
                //PositionUpdateTask lTask = new PositionUpdateTask(this, mTargetEntity);
                //ThreadTaskExecutor.submitTask(lTask);
                this.setPos(mTargetEntity.getX(), mTargetEntity.getY() + 2f, mTargetEntity.getZ());
            }



            /*
            double targetX = mTargetEntity.getX();
            double targetY = mTargetEntity.getY() + 2.0; // Example offset of 1.0 block above
            double targetZ = mTargetEntity.getZ();

            // Smooth transition using lerping (linear interpolation)
            double lerpingFactor = 0.1; // Adjust this value for smoother or faster transitions
            double newX = this.getX() + (targetX - this.getX()) * lerpingFactor;
            double newY = this.getY() + (targetY - this.getY()) * lerpingFactor;
            double newZ = this.getZ() + (targetZ - this.getZ()) * lerpingFactor;

            this.setPos(newX, newY, newZ);

             */

/*
            double targetX = mTargetEntity.getX();
            double targetY = mTargetEntity.getY();
            double targetZ = mTargetEntity.getZ();

            // Calculate the velocity to move smoothly towards the target
            double speed = 0.1; // Adjust the speed factor as needed
            double dx = (targetX - this.getX()) * speed;
            double dy = (targetY - this.getY()) * speed;
            double dz = (targetZ - this.getZ()) * speed;

            // Apply the velocity to the entity
            this.setDeltaMovement(dx, dy + 2f, dz);
            this.moveTo(this.getDeltaMovement());


 */
          //  Vec3 lEntityPos = mTargetEntity.position();
          //  Vec3 lNewPos = new Vec3(lEntityPos.x, lEntityPos.y + 2f, lEntityPos.z);
            //Vec3 lNewPos = new Vec3(mTargetEntity.getX(), mTargetEntity.getY() + 2f, mTargetEntity.getZ());
         //   System.out.println("oldPos: " + this.position());
           // System.out.println("newPos: " + lNewPos + "\n");
            //this.setPos(lNewPos);


            //this.setDeltaMovement(lNewPos);
            //this.move(MoverType.SELF, lNewPos);

            //this.setDeltaMovement(lNewPos);

                //System.out.println("SetPos");
            }
        }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (this.isAlive() && this.tickCount - tickChecker >= mCoolDownForDespawn) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
       return NetworkHooks.getEntitySpawningPacket(this);
   }

}

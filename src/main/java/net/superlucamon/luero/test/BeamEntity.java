package net.superlucamon.luero.test;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BeamEntity extends PathfinderMob {
    private static Player mPlayer;
    private static Vec3 mEnd;

    public BeamEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.hasImpulse = true;
        this.markHurt();
        this.noPhysics = true;
        registerGoals();
    }


    public static void moveEntityBehindPlayer(Entity entity, Player player) {
        Vec3 playerPos = player.position();
        Vec3 lookVec = player.getLookAngle();
        float playerYaw = (float) -(Mth.atan2(lookVec.z, lookVec.x) * (180f / Math.PI)) - 90;

        double directionX = -Mth.sin(((float) player.getXRot()) * ((float)Math.PI / 180));
        double directionZ = Mth.cos(((float) player.getZ()) * ((float)Math.PI / 180));

        double offsetX = directionX * 2.0;
        double offsetZ = directionZ * 1.0;

        double entityX = playerPos.x + offsetX;
        double entityY = playerPos.y;
        double entityZ = playerPos.z + offsetZ;

        // Alternatively, you can set yaw to the player's body rotation (getYHeadRot() or getYRot())


        // Set the entity's position
     //   entity.setPos(entityX, entityY, entityZ);
    }
    public static AttributeSupplier.Builder createSentryAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25f);

    }

    @Override
    public boolean hasCustomOutlineRendering(Player player) {
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();

    }

    @Override
    public void tick() {
        super.tick();
        if (mPlayer != null) {
            //Vec3 start = mPlayer.getPosition(1);
           // this.setPos(mPlayer.position().x, mPlayer.position().y + 1.25f, mPlayer.position().z);

           /* // Fixed pitch and yaw values for the direction you want to raycast
            // For example, we can set pitch to 0 and yaw to 0 for forward direction
            float pitch = 0.0F; // No vertical angle
            float yaw = 0.0F; // No horizontal angle

            // Alternatively, you can set yaw to the player's body rotation (getYHeadRot() or getYRot())
            yaw = mPlayer.getYHeadRot();

            float pitchRad = (float) Math.toRadians(pitch);
            float yawRad = (float) Math.toRadians(yaw);

            double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
            double dy = -Math.sin(pitchRad);
            double dz = Math.cos(yawRad) * Math.cos(pitchRad);

            // Create the direction vector
            Vec3 direction = new Vec3(dx, dy + -3.25, dz);

            // Extend the direction vector to get the end position for raycasting
            double reachDistance = -0.4D; // Example reach distance
            Vec3 end = start.add(direction.scale(reachDistance));

            */
           // setPos(end);
           // System.out.println(pitch);
            //setPos(mPlayer.position().x, mPlayer.position().y + 1.25f, mPlayer.position().z);
            //moveEntityBehindPlayer(this, mPlayer);
            //facePlayer(mPlayer);

            Vec3 lookVec = mPlayer.getLookAngle();
           // float yaw = (float) -(Mth.atan2(lookVec.z, lookVec.x) * (180f / Math.PI)) - 90;
            float pitch = (float) ((Mth.atan2(lookVec.y, Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z)) * (180 / Math.PI))-90);
            if (pitch >= -134){
                this.hasImpulse = true;
                this.markHurt();
                this.moveTo(mPlayer.position().x, mPlayer.position().y + 1.25f, mPlayer.position().z);
            }
            if (pitch <= -134){
                this.hasImpulse = true;
                this.markHurt();
                Vec3 start = mPlayer.position();
                float pitcht = 0.0F;
                float yawt = 0.0F;

                // Alternatively, you can set yaw to the player's body rotation (getYHeadRot() or getYRot())
                yawt = mPlayer.getYHeadRot();

                float pitchRad = (float) Math.toRadians(pitcht);
                float yawRad = (float) Math.toRadians(yawt);

                double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
                double dy = -Math.sin(pitchRad);
                double dz = Math.cos(yawRad) * Math.cos(pitchRad);

                // Create the direction vector
                Vec3 direction = new Vec3(dx, dy + -5.0, dz);

                // Extend the direction vector to get the end position for raycasting
                double reachDistance = -0.3D; // Example reach distance
                Vec3 end = start.add(direction.scale(reachDistance));

                this.setPos(end);


            }

        }
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }




    public void setPlayer(Player player) {
        mPlayer = player;
    }
    private void facePlayer(Player player) {
        // Get the direction the player is looking
        Vec3 lookVec = player.getLookAngle();

        double dx = player.getX() - this.getX();
        double dy = player.getEyeY() - this.getEyeY();
        double dz = player.getZ() - this.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        float newYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90;
        float newPitch = (float) -(Math.atan2(dy, distance) * (180 / Math.PI));

        // Set entity's yaw and pitch
        this.setYRot(newYaw);
        this.setXRot(newPitch);

        // Update entity's rotation vector
       this.setYBodyRot(newYaw);
       this.setYHeadRot(newYaw);
       this.yRotO = newYaw;
       this.yo = newYaw;


        // Calculate yaw and pitch
       /* float playerYaw = player.getYRot();
        float playerPitch = player.getXRot();

// Calculate the direction vector based on player's rotation
        float pitchRad = (float) Math.toRadians(playerPitch);
        float yawRad = (float) Math.toRadians(playerYaw);

        double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dy = -Math.sin(pitchRad);
        double dz = Math.cos(yawRad) * Math.cos(pitchRad);



// Calculate the new yaw and pitch based on the player's look direction
        float newYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90;
        float newPitch = (float) -(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));

        */

// Apply the new yaw and pitch to the entity
        this.setYRot(newYaw);
        this.setXRot(newPitch);

// Update the entity's rotation vector
        this.setYBodyRot(newYaw);
        this.setYHeadRot(newYaw);
        this.yRotO = newYaw;
        this.yo = newYaw;
    }
}
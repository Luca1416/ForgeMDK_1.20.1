package net.superlucamon.luero.heros.ironman.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.entity.types.nonhostile.SentryEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.superlucamon.luero.test.CustomEntityRegister.SENTRY_ENTITY;

@Mod.EventBusSubscriber(modid = "heromod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SentryMode {
    private static SentryEntity mSentry;
    private static ServerPlayer mPlayer;
    private static float  speed = 1;
    private static Path path;
    private static boolean callback;
    private static final int cd = 2;
    private static int ticksSinceArrival;
    private static int test;
    private static final Map<UUID, SentryEntity> activeSentries = new HashMap<>();

    public static boolean hasSentry(ServerPlayer player) {
        return activeSentries.containsKey(player.getUUID());
    }


    public static void spawnSentry(ServerPlayer player) {
        mSentry = new SentryEntity(SENTRY_ENTITY.get(), player.level());
        Vec3 spawnPosition = player.position();
        mSentry.setPos(spawnPosition.x, spawnPosition.y- 1, spawnPosition.z);
        player.level().addFreshEntity(mSentry);
        mPlayer = player;
        activeSentries.put(player.getUUID(), mSentry);
        System.out.println(mSentry.position());
    }

    public static boolean isCallback() {
        return callback;
    }

    public static void callbackSentry(ServerPlayer pPlayer) {
       if (mSentry != null) {
           if (!callback) {
               callback = true;
               mSentry.isReturing = true;
               speed = 2;
               mSentry.transformingToPlayer = false;
               pPlayer.displayClientMessage(Component.literal("Calling back Sentry...").withStyle(style -> style.withColor(0x89CFF0)).withStyle(style -> style.withBold(true)), true);
           }
           else if(callback) {
               callback = false;
               path = null;
               mSentry.isReturing = false;
               mSentry.noPhysics = false;
               mSentry.readyForTransform = false;
               mSentry.transformingToPlayer = false;
               speed = 1f;
               mSentry.setNoAi(false);
           }
       }
        System.out.println("Callback: " + callback);
    }
    @SubscribeEvent
    public static void onSentryClicked(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() == (LivingEntity) mSentry) {
            System.out.println("yo");
            if (mPlayer.isShiftKeyDown()) {
                mSentry.readyForTransform = false;
                mSentry.noPhysics = false;
                mSentry.horizontalCollision = false;
                mSentry.setNoAi(false);
            } else {
               // mSentry.discard();
                mSentry.transformingToPlayer = true;
            }
        }
    }
    @SubscribeEvent
    public static void callBackTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && mSentry != null) {
            if (!mSentry.isAlive()){
                mSentry = null;
            }
            if (mSentry.transformingToPlayer){
                mSentry.walkDistO = 0;
                mSentry.noPhysics = true;
                Vec3 direction = new Vec3(
                        mPlayer.getX() - mSentry.getX(),
                        mPlayer.getY() - mSentry.getY(),
                        mPlayer.getZ() - mSentry.getZ()
                ).normalize();
                Vec3 movement = direction.scale(0.35f);
                mSentry.setPos(mSentry.getX() + movement.x, mSentry.getY() + movement.y, mSentry.getZ() + movement.z);
                if (mSentry.distanceTo(mPlayer) < 0.065f){
                    mSentry.discard();
                    mSentry.noPhysics = false;
                    mSentry.transformingToPlayer = false;
                    activeSentries.remove(mPlayer.getUUID());
                }
            }
            if (callback){
                path = mSentry.getNavigation().createPath(mPlayer, 0);
                mSentry.getLookControl().setLookAt(mPlayer, 30.0F, 30.0F);
                mSentry.getNavigation().moveTo(path, speed);
                if (mSentry.distanceTo(mPlayer) < 5){
                    speed = 1f;
                }
                else speed = 2f;
                if (mSentry.distanceTo(mPlayer) < 2){
                    mSentry.readyForTransform = true;
                    mSentry.setNoAi(true);
                    mSentry.setYHeadRot(mPlayer.getYHeadRot());
                    mSentry.setYRot(mPlayer.getYRot());
                    mSentry.setXRot(30);
                    ticksSinceArrival = 0;
                    callback = false;
                    mSentry.isReturing = false;
                    System.out.println(mSentry.readyForTransform);
                }
            }
        }
    }
    public static String getAbilityName(ServerPlayer player) {
        return hasSentry(player) ? "Recall Sentry" : "Spawn Sentry";
    }
}

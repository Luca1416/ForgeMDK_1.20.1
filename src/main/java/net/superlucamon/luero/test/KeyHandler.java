package net.superlucamon.luero.test;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = "heromod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyHandler {

//    public static long windowHandle = Minecraft.getInstance().getWindow().getWindow();
    public static final KeyMapping TEST_MAP_KEY = new KeyMapping("key.testmod_v1.abilityxray", GLFW.GLFW_KEY_G, "key.categories.testmod_v1");
    public static final KeyMapping TEST_ACTIVATE_KEY = new KeyMapping("key.testmod_v1.ability", GLFW.GLFW_KEY_K, "key.categories.testmod_v1");
    private static LivingEntity mEntity;
    public static Set<LivingEntity> map = new HashSet<>();



    private static boolean mXRayEnabled = false;


    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key keyEvent) {
        if (TEST_MAP_KEY.isDown()) {
            // System.out.println("got KeyInput");
            /*
            if (TestItem.checkIfLookingAtEntity() != null) {
                mEntity = TestItem.checkIfLookingAtEntity();
                if (mEntity != null) {
                    map.add(mEntity);
                    System.out.println(map);
                    //for (LivingEntity entity : map) {

                      //  marker.setPos(entity.getX(), entity.getY() + 2, entity.getZ());
                       // entity.level().addFreshEntity(marker);
                    //}
                    CustomSmallFireball marker = new CustomSmallFireball(CUSTOM_SMALL_FIREBALL.get(), mEntity.level());
                    marker.setTargetEntity(mEntity);
                    marker.setOwner(mEntity);
                    marker.setNoGravity(true);
                    mEntity.level().addFreshEntity(marker);

                }
            }

             */
        } else {
            /*
            if (mEntity != null) {
                map.add(mEntity);
                System.out.println(map);
            }

             */

        }
        if (TEST_ACTIVATE_KEY.consumeClick()) {
            /*
            for (LivingEntity entity : map) {
              CustomSmallFireball marker = new CustomSmallFireball(CUSTOM_SMALL_FIREBALL.get(), entity.level());
                marker.setTargetEntity(entity);
                marker.setOwner(entity);
                marker.setNoGravity(true);
                entity.level().addFreshEntity(marker);
            }

             */
        }
    }
}
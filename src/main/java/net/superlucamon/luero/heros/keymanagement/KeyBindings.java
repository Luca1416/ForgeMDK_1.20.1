package net.superlucamon.luero.heros.keymanagement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {
    public static final KeyMapping UNIVERSEL_RIGHTCLICK = new KeyMapping(
            "key.heromod.universel_rightclick",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_KEY_G,
            null

    );
    public static final KeyMapping ABILITYKEY_1 = new KeyMapping(
            "key.heromod.abilitykey_1",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.heromod"

    );
    public static final KeyMapping ABILITYKEY_2 = new KeyMapping(
            "key.heromod.callback_sentry",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_SHIFT & GLFW.GLFW_KEY_RIGHT_SHIFT,
            "key.categories.heromod"
    );

    public static final KeyMapping ABILITYKEY_3 = new KeyMapping(
            "key.heromod.sentry_spawn",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.heromod"

    );
    public static final KeyMapping UNIVSERAL_TEST = new KeyMapping(
            "key.heromod.universal_test",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.categories.heromod"

    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ABILITYKEY_3);
        event.register(ABILITYKEY_2);
    }
}

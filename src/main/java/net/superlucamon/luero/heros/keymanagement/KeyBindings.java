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
    public static final KeyMapping RIGHTCLICK = new KeyMapping(
            "key.heromod.universel_rightclick",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_2,
            "key.categories.heromod"

    );
    public static final KeyMapping ABILITYKEY_1 = new KeyMapping(
            "key.heromod.abilitykey_1",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.heromod"

    );
    public static final KeyMapping ABILITYKEY_2 = new KeyMapping(
            "key.heromod.abilitykey_2",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F,
            "key.categories.heromod"
    );

    public static final KeyMapping ABILITYKEY_3 = new KeyMapping(
            "key.heromod.abilitykey_3",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.heromod"

    );
    public static final KeyMapping ABILITYKEY_4 = new KeyMapping(
            "key.heromod.abilitykey_4",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
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
        event.register(RIGHTCLICK);
        event.register(UNIVSERAL_TEST);
        event.register(ABILITYKEY_1);
        event.register(ABILITYKEY_2);
        event.register(ABILITYKEY_3);
        event.register(ABILITYKEY_4);
    }
}

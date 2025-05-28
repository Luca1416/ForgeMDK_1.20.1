package net.superlucamon.luero.heros.keymanagement;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.networking.ModPackets;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.ABILITYKEY_1.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(0)); // Ability 0
        }
        if (KeyBindings.ABILITYKEY_2.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(1)); // Ability 1
        }
        if (KeyBindings.ABILITYKEY_3.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(2)); // Ability 2
        }
        if (KeyBindings.ABILITYKEY_4.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(3)); // Ability 3
        }
    }
}

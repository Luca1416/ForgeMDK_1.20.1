package net.superlucamon.luero.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.heros.keymanagement.AbilityKeyPressPacket;
import net.superlucamon.luero.heros.keymanagement.KeyBindings;
import net.superlucamon.luero.networking.ModPackets;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class C2SKeyHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.ABILITYKEY_1.isDown()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(0));
        }
        if (KeyBindings.ABILITYKEY_2.isDown()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(1));
        }
        if (KeyBindings.ABILITYKEY_3.isDown()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(2));
        }
        if (KeyBindings.ABILITYKEY_4.isDown()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(3));
        }
    }
}

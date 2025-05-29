package net.superlucamon.luero.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.heros.keymanagement.KeyBindings;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.server.AbilityKeyPressPacket;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class ClientAbilityInputHandler {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (KeyBindings.ABILITYKEY_1.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(0));
        }
        if (KeyBindings.ABILITYKEY_2.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(1));
        }
        if (KeyBindings.ABILITYKEY_3.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(2));
        }
        if (KeyBindings.ABILITYKEY_4.consumeClick()) {
            ModPackets.sendToServer(new AbilityKeyPressPacket(3));
        }
    }
}
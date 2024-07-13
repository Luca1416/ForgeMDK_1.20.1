package net.superlucamon.luero.heros.keymanagement;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.heros.ironman.abilities.SentryMode;
import net.superlucamon.luero.item.custom.TestItem;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.ABILITYKEY_3.consumeClick()) {
                if (TestItem.mPlayer != null) {
                    SentryMode.spawnSentry((ServerPlayer) TestItem.mPlayer);
                }
        }
        if (KeyBindings.ABILITYKEY_2.consumeClick()) {
            if (TestItem.mPlayer != null) {
                SentryMode.callbackSentry((ServerPlayer) TestItem.mPlayer);
            }
        }
    }
}

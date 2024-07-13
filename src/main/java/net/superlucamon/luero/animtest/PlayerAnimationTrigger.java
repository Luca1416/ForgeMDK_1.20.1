package net.superlucamon.luero.animtest;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerAnimationTrigger {

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        if (event.getMessage().contains(Component.literal("waving"))) {

            var player = Minecraft.getInstance().level.getPlayerByUUID(event.getSender());

            if (player == null) return;

            ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>)PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(Main.MOD_ID, "animation"));
            if (animation != null) {
                animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("heromod", "waving"))));
            }
        }
    }
}

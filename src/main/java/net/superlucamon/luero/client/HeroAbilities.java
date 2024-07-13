package net.superlucamon.luero.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.heros.keymanagement.KeyBindings;

import java.awt.*;

public class HeroAbilities {
private static final ResourceLocation test = new ResourceLocation(Main.MOD_ID, "textures/ironmanabilities/ability_test.png");
    public static final IGuiOverlay IRONMAN_ABILITIES_HUD = (((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(100,255,1,1);
        RenderSystem.setShaderTexture(0, test);
        guiGraphics.drawString(Minecraft.getInstance().font,"Missle: " + KeyBindings.ABILITYKEY_1.getKey().getDisplayName().getString(), 1, 1, new Color(255,255,255).hashCode(), false);
        guiGraphics.drawString(Minecraft.getInstance().font,"Beam: V", 1, 10, new Color(255,255,255).hashCode(), false);
        guiGraphics.drawString(Minecraft.getInstance().font,"Fly: Shift+B", 1, 20, new Color(255,255,255).hashCode(), false);
    }));
}

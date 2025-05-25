package net.superlucamon.luero.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.heros.keymanagement.KeyBindingValues;
import net.superlucamon.luero.server.HeroRegistry;

import java.awt.*;

public class HeroAbilities {
private static final ResourceLocation test = new ResourceLocation(Main.MOD_ID, "textures/ironmanabilities/ability_test.png");
    public static final IGuiOverlay IRONMAN_ABILITIES_HUD = (((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Player player = Minecraft.getInstance().player;
        if (player != null && ClientHeroData.getPlayerHero() != null) {
            String heroCapability = ClientHeroData.getPlayerHero();
            if (heroCapability != null && HeroRegistry.getHeroByName(heroCapability) != null) {
                for (int i = 0; i < HeroRegistry.getHeroByName(heroCapability).getAbilities().size(); i++) {
                    String abilityName = HeroRegistry.getHeroByName(heroCapability).getAbilities().get(i).getName();
                    String keyName = KeyBindingValues.values()[i].getFormattedKey();
                    drawAbilities(guiGraphics, keyName," - " + abilityName, 10, 10 + i * 11, 1, (int)(0.2f * 255) << 24 | 0 << 16 | 0 << 8 | 0,
                            new Color(150, 150, 150).hashCode(),
                            HeroRegistry.getHeroByName(heroCapability).getAbilityTextColor().hashCode());
                }
            }
        }
    }));
    public static void drawAbilities(GuiGraphics guiGraphics, String keyValue, String abilityName, int x, int y, int boxSize, int boxColor, int textColorAbilityName, int textColorKeyName) {
        Font font = Minecraft.getInstance().font;

        boolean isModifierKey = keyValue.contains("+");

        int spacing = isModifierKey ? 2 : -2; // Shorter spacing for single keys
        int keyWidth = font.width(keyValue);
        int abilityWidth = font.width(abilityName);
        int totalTextWidth = keyWidth + spacing + abilityWidth;

        int textHeight = font.lineHeight;
        int boxX = x - boxSize;
        int boxY = y - boxSize;
        int boxWidth = totalTextWidth + 2 * boxSize;
        int boxHeight = textHeight + 2 * boxSize;

        guiGraphics.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, boxColor);
        guiGraphics.drawString(font, keyValue, x, y, textColorKeyName, true);
        guiGraphics.drawString(font, abilityName, x + keyWidth + spacing, y, textColorAbilityName, false);
    }

}

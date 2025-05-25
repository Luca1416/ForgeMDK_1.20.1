package net.superlucamon.luero.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.superlucamon.luero.client.ClientHeroData;
import net.superlucamon.luero.server.HeroManager;
import net.superlucamon.luero.server.HeroProvider.Hero;
import net.superlucamon.luero.server.HeroRegistry;

public class ArmorChecker {
    public static void checkAndSetHero(Player player) {
        ItemStack[] equippedArmor = {
                player.getInventory().getArmor(3), // Helmet slot
                player.getInventory().getArmor(2), // Chestplate slot
                player.getInventory().getArmor(1), // Leggings slot
                player.getInventory().getArmor(0)  // Boots slot
        };

        for (Hero hero : HeroRegistry.HEROES.values()) {
            if (isFullSet(equippedArmor, hero.getArmorSet())) {
                HeroManager.setPlayerHero(player, hero);
                ClientHeroData.set(hero.getName());
                //System.out.println("working");
                return; // return after setting the hero
            }
        }
        HeroManager.setPlayerHero(player,null); // No matching hero
        ClientHeroData.set(null);
    }

    private static boolean isFullSet(ItemStack[] equippedArmor, ItemStack[] heroArmor) {
        if (equippedArmor.length != heroArmor.length) {
            return false;
        }

        for (int i = 0; i < equippedArmor.length; i++) {
            if (!ItemStack.isSameItemSameTags(equippedArmor[i], heroArmor[i])) {
                return false; // Mismatch in any armor piece
            }
        }

        return true; // All armor pieces match
    }
}
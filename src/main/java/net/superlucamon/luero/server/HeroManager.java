package net.superlucamon.luero.server;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.superlucamon.luero.server.HeroProvider.Ability;
import net.superlucamon.luero.server.HeroProvider.Hero;

public class HeroManager {
    public static void setPlayerHero(Player player, Hero hero) {
        player.getCapability(HeroCapabilityProvider.HERO_CAPABILITY).ifPresent(cap -> {
            cap.setCurrentHero(hero);
            updatePlayerAbilities(player, hero);
        });
    }
    public static Hero getPlayerHero(Player player) {
        IHeroCapability cap = player.getCapability(HeroCapabilityProvider.HERO_CAPABILITY).orElse(null);
        return cap != null ? cap.getCurrentHero() : null;
    }


    public static void updatePlayerAbilities(Player player, Hero hero) {
        player.sendSystemMessage(Component.literal("You are " + hero.getName() + " with abilities:"));
        for (Ability ability : hero.getAbilities()) {
            player.sendSystemMessage(Component.literal("- " + ability.getName() + ": " + ability.getDescription()));
        }
    }
}

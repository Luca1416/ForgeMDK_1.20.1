package net.superlucamon.luero.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;
import java.util.*;

public class HeroProvider {
    public static class Ability {
        private String name;
        private String description;

        public Ability(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
    public abstract static class Hero {
        private String name;
        private Color abilityTextColor;
        private List<Ability> abilities;
        // In HeroProvider.Hero:
        private final Map<Integer, AbilityHandler> abilityHandlers = new LinkedHashMap<>();
        private final Map<Integer, Ability> keyAbilityMap = new LinkedHashMap<>();
        private final Map<Integer, Map<UUID, Long>> abilityCooldowns = new HashMap<>();

        public Hero(String name, Color abilityTextColor, List<Ability> abilities) {
            this.name = name;
            this.abilities = abilities;
            this.abilityTextColor = abilityTextColor;
        }
        //public abstract void activateAbility();

        public void bind(int index, Ability ability, AbilityHandler handler) {
            this.abilityHandlers.put(index, handler);
        }


        public Map<Integer, AbilityHandler> getBindings() {
            return abilityHandlers;
        }
        public void activateAbility(int index, ServerPlayer player) {
            AbilityHandler handler = this.abilityHandlers.get(index);
            if (handler != null) {
                handler.handle(player);
            }
        }

        // Add this method
        public boolean isOnCooldown(int abilityIndex, ServerPlayer player, long cooldownMillis) {
            Map<UUID, Long> slotMap = abilityCooldowns.computeIfAbsent(abilityIndex, i -> new HashMap<>());
            long now = System.currentTimeMillis();
            Long until = slotMap.get(player.getUUID());
            if (until != null && now < until) {
                return true; // still cooling down
            }
            // set new cooldown
            slotMap.put(player.getUUID(), now + cooldownMillis);
            return false;
        }

        public Map<Integer, Ability> getAbilityMappings() {
            return keyAbilityMap;
        }
        public abstract void init();
        public abstract ItemStack[] getArmorSet();

        public Color getAbilityTextColor() {
            return abilityTextColor;
        }
        public String getName() {
            return name;
        }
        public List<Ability> getAbilities() {
            return abilities;
        }
    }
    @FunctionalInterface
    public interface AbilityHandler {
        void handle(ServerPlayer player);
    }

}

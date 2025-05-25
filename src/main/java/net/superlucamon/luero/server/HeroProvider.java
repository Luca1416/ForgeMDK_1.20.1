package net.superlucamon.luero.server;

import net.minecraft.client.KeyMapping;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        private final Map<KeyMapping, AbilityHandler> abilityMap = new LinkedHashMap<>();
        private final Map<KeyMapping, Ability> keyAbilityMap = new LinkedHashMap<>();

        public Hero(String name, Color abilityTextColor, List<Ability> abilities) {
            this.name = name;
            this.abilities = abilities;
            this.abilityTextColor = abilityTextColor;
        }
        //public abstract void activateAbility();

        public void bind(KeyMapping key, Ability ability, AbilityHandler handler) {
            abilityMap.put(key, handler);
            keyAbilityMap.put(key, ability);
        }

        public Map<KeyMapping, AbilityHandler> getBindings() {
            return abilityMap;
        }

        public Map<KeyMapping, Ability> getAbilityMappings() {
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
    public interface AbilityHandler {
        void trigger(ServerPlayer player);
    }

}

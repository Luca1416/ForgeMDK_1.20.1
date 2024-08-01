package net.superlucamon.luero.server;

import java.awt.*;
import java.util.List;

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
    public static class Hero {
        private String name;
        private Color abilityTextColor;
        private List<Ability> abilities;
        public Hero(String name, Color abilityTextColor, List<Ability> abilities) {
            this.name = name;
            this.abilities = abilities;
            this.abilityTextColor = abilityTextColor;
        }

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
}

package net.superlucamon.luero.client;

import java.util.List;

public class CSHeroProvider {
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
        private List<Ability> abilities;
        public Hero(String name, List<Ability> abilities) {
            this.name = name;
            this.abilities = abilities;
        }

        public String getName() {
            return name;
        }
        public List<Ability> getAbilities() {
            return abilities;
        }
    }
}

package net.superlucamon.luero.client;

import net.superlucamon.luero.client.CSHeroProvider.Hero;
import net.superlucamon.luero.client.CSHeroProvider.Ability;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HeroRegistry {
    private static final Map<String, Hero> HEROES = new HashMap<>();

    public static void registerHero(Hero hero) {
        HEROES.put(hero.getName(), hero);
    }

    public static Hero getHeroByName(String name) {
        return HEROES.get(name);
    }
    public static void initializeHeroes() {
        registerHero(new Hero("IronMan", Arrays.asList(new CSHeroProvider.Ability("Missle", ""), new Ability("SentryMode", ""))));
        registerHero(new Hero("Hero2", Arrays.asList(new CSHeroProvider.Ability("AbilityA", "DescriptionA"), new Ability("AbilityB", "DescriptionB"))));
    }
}

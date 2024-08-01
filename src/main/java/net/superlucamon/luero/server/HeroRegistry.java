package net.superlucamon.luero.server;

import net.superlucamon.luero.server.HeroProvider.Ability;
import net.superlucamon.luero.server.HeroProvider.Hero;

import java.awt.*;
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
        //Ironman
        registerHero(new Hero("IronMan", new Color(255, 215, 0), Arrays.asList(new HeroProvider.Ability("Missle", "Shoots homing missiles"),
                new Ability("SentryMode", "Spawns a sentry using your armor"),
                new Ability("test", "tests"),
                new Ability("tests", "tests"))));
        //Deadpool
        registerHero(new Hero("DeadPool", new Color(139, 0, 0), Arrays.asList(new HeroProvider.Ability("Knife Stab", "Stabs near enemies and add blood effect to them"),
                new Ability("Knife throw", "Throws a knife in the direction the AntiHero is looking"),
                new Ability("test", "tests"))));
    }
}

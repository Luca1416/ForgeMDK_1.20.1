package net.superlucamon.luero.server;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.superlucamon.luero.entity.projectile.UniBeamEntity;
import net.superlucamon.luero.heros.ironman.abilities.SentryMode;
import net.superlucamon.luero.heros.keymanagement.KeyBindings;
import net.superlucamon.luero.item.ModItems;
import net.superlucamon.luero.server.HeroProvider.Ability;
import net.superlucamon.luero.server.HeroProvider.Hero;
import net.superlucamon.luero.test.CustomEntityRegister;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HeroRegistry {
    public static final Map<String, Hero> HEROES = new HashMap<>();
    public static void registerHero(Hero hero) {
        HEROES.put(hero.getName(), hero);
    }
    public static Hero getHeroByName(String name) {
        return name != null ? HEROES.get(name) : null;
    }
    public static void initializeHeroes() {
        //Ironman
        registerHero(new Hero("IronMan", new Color(255, 215, 0), Arrays.asList(new Ability("Missle", "Shoots homing missiles"),
                new Ability("SentryMode", "Spawns a sentry using your armor"),
                new Ability("Unibeam", "Shoots a unibeam out your chest"),
                new Ability("Repulsor", "Shoots a repulsor out of right hand"))) {
            private boolean uniBeam;
            @Override
            public void init() {
                this.bind(KeyBindings.ABILITYKEY_1, getAbilities().get(0), player -> {
                    // TODO: Implement Missile ability
                    System.out.println("IronMan: Missile");
                });
                this.bind(KeyBindings.ABILITYKEY_2, getAbilities().get(1), player -> {
                    if (!SentryMode.hasSentry(player))
                    {
                        SentryMode.spawnSentry(player);
                    }
                    else if (!SentryMode.isCallback())
                    {
                        SentryMode.callbackSentry(player);
                    }
                    System.out.println("IronMan: SentryMode");
                });
                this.bind(KeyBindings.ABILITYKEY_3, getAbilities().get(2), player -> {
                    uniBeam = !uniBeam;
                    if (uniBeam) {
                        Vec3 look = player.getLookAngle(); // normalized direction
                        Vec3 origin = player.position().add(0, 1.3, 0); // chest height

                        UniBeamEntity beam = new UniBeamEntity(CustomEntityRegister.CUSTOM_BEAM.get(), player.level());
                        beam.setPlayer(player);
                        beam.setPos(origin.x, origin.y, origin.z);
                        beam.setDeltaMovement(look.scale(1)); // optional
                        beam.setYRot((float) (Math.toDegrees(Math.atan2(look.z, look.x)) - 90));
                        beam.setXRot((float) (-Math.toDegrees(Math.asin(look.y))));
                        player.level().addFreshEntity(beam);
                    }
                });
                this.bind(KeyBindings.ABILITYKEY_4, getAbilities().get(3), player -> {
                    // TODO: Implement Repulsor
                    System.out.println("IronMan: Repulsor");
                });
            }


            @Override
            public ItemStack[] getArmorSet() {
                return new ItemStack[]{
                        new ItemStack(ModItems.IRONMAN_HELMET_MK47.get()),
                        new ItemStack(ModItems.IRONMAN_CHESTPLATE_MK47.get()),
                        new ItemStack(ModItems.IRONMAN_LEGGINGS_MK47.get()),
                        new ItemStack(ModItems.IRONMAN_BOOTS_MK47.get())
                };
            }
        });
        //Deadpool
        registerHero(new Hero("DeadPool", new Color(139, 0, 0), Arrays.asList(new Ability("Knife Stab", "Stabs near enemies and add blood effect to them"),
                new Ability("Knife Throw", "Throws a knife in the direction the AntiHero is looking"),
                new Ability("Exploding Knife", "Throws a exploding knife in the direction the AntiHero is looking"))) {
            @Override
            public void init()
            {
                //System.out.println("DeadPool initialized");
            }
            @Override
            public ItemStack[] getArmorSet() {
                return new ItemStack[]{
                };
            }
        });
    }
}

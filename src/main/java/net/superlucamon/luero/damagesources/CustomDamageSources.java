package net.superlucamon.luero.damagesources;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class CustomDamageSources {
    public static final ResourceKey<DamageType> LASER;


    public CustomDamageSources() {
    }
    public static DamageSource missle(Entity shooter) {
        return new DamageSource(shooter.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LASER), (Entity)null, shooter);
    }

    static {
        LASER = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("heromod", "missle"));
    }
}
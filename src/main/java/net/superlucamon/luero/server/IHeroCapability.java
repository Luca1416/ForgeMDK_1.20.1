package net.superlucamon.luero.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.superlucamon.luero.server.HeroProvider.Hero;

public interface IHeroCapability extends INBTSerializable<CompoundTag> {
    Hero getCurrentHero();
    void setCurrentHero(Hero hero);
}

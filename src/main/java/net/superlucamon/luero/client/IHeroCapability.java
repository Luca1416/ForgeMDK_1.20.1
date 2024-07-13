package net.superlucamon.luero.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.superlucamon.luero.client.CSHeroProvider.Hero;

public interface IHeroCapability extends INBTSerializable<CompoundTag> {
    Hero getCurrentHero();
    void setCurrentHero(Hero hero);
}

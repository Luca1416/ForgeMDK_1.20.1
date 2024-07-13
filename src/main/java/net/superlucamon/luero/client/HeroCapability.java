package net.superlucamon.luero.client;

import net.minecraft.nbt.CompoundTag;
import net.superlucamon.luero.client.CSHeroProvider.Hero;

public class HeroCapability implements IHeroCapability {
    private Hero currentHero;
    @Override
    public Hero getCurrentHero() {
        return currentHero;
    }

    @Override
    public void setCurrentHero(Hero hero) {
        this.currentHero = hero;

    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (currentHero != null) {
            tag.putString("HeroName", currentHero.getName());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("HeroName")) {
            String heroName = nbt.getString("HeroName");
            this.currentHero = HeroRegistry.getHeroByName(heroName);
        }
    }
}

package net.superlucamon.luero.item.client;

import net.minecraft.resources.ResourceLocation;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.item.custom.IronmanMark1TestArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class IronmanMark1TestModel extends GeoModel<IronmanMark1TestArmorItem>  {
    @Override
    public ResourceLocation getModelResource(IronmanMark1TestArmorItem animatable) {
        return new ResourceLocation(Main.MOD_ID, "geo/ironman_mark1test.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IronmanMark1TestArmorItem animatable) {
        return new ResourceLocation(Main.MOD_ID, "textures/armor/ironman_mark1test.png");
    }

    @Override
    public ResourceLocation getAnimationResource(IronmanMark1TestArmorItem animatable) {
        return new ResourceLocation(Main.MOD_ID, "animations/ironman_mark1test.animation.json");
    }
}



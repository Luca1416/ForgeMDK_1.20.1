package net.superlucamon.luero.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.superlucamon.luero.heros.keymanagement.KeyBindings;
import net.superlucamon.luero.item.custom.IronmanMark1TestArmorItem;
import org.joml.Math;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import javax.annotation.Nullable;

public class IronmanMark1TestRenderer extends GeoArmorRenderer<IronmanMark1TestArmorItem> {
    private static float currentScale = 0.4f;
    private static float deltaTick = 0f;
    private static boolean suitOn = true;
    private static float mAlpha = 0f;

    public IronmanMark1TestRenderer() {
        super(new IronmanMark1TestModel());
    }

    @Override
    public RenderType getRenderType(IronmanMark1TestArmorItem animatable, ResourceLocation texture, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
        //return RenderType.entityTranslucentCull(texture);
        //return RenderType.armorCutoutNoCull(texture);
    }

    @Override
    public void preRender(PoseStack poseStack, IronmanMark1TestArmorItem animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource,
                          @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
                          int packedOverlay, float red, float green, float blue, float alpha) {



        if (suitOn && currentScale < 0.95f) {
            ++deltaTick;
            //currentScale = 0;
            //if (currentScale < 1) {

            currentScale = Math.lerp(deltaTick * 0.004f, currentScale, 0.025f);

            poseStack.scale(currentScale, currentScale, currentScale);
            //  }
        }
        //if (this.currentEntity instanceof Player && currentScale >= 0 && !on) {
        // currentScale = Math.lerp(deltaTick * 0.02f, currentScale, 0.05f);
        // System.out.println(currentScale);
        //}
        if (KeyBindings.ABILITYKEY_1.consumeClick()) {
            mAlpha = 0f;
            suitOn = !suitOn;
            if (!suitOn) {
                currentScale = 0.4f;
                poseStack.scale(currentScale, currentScale, currentScale);
            } else {
                deltaTick = 0;
            }
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, IronmanMark1TestArmorItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (suitOn) {

            /*
            if (suitOn && mAlpha < 1f) {
                mAlpha = Math.lerp(deltaTick * 0.001f, mAlpha, 0.025f);
            }
            alpha = mAlpha;
             */

            super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}


package net.superlucamon.luero.test;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.entity.models.CustomMissileModel;

@Mod.EventBusSubscriber(modid = "heromod", value = Dist.CLIENT)
public class CustomMissileRenderer extends MobRenderer<CustomFlyingRocket, CustomMissileModel<CustomFlyingRocket>> {
    private static final ResourceLocation spriteTexture = new ResourceLocation("heromod", "textures/entity/custom_missile_texture.png");
    public CustomMissileRenderer(EntityRendererProvider.Context context) {
        super(context, new CustomMissileModel<>(context.bakeLayer(ModModelLayers.FIREBALL_LAYER)), 0.2f);
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderLivingEvent.Post<LivingEntity, ?>  event) {
    }

    @Override
    public void render(CustomFlyingRocket entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        float scale = 1F;
            matrixStack.scale(scale, scale, scale);
          //  matrixStack.mulPose(Axis.ZP.rotationDegrees(entity.tickCount * Mth.PI * 2.0F));
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }



    @Override
    public ResourceLocation getTextureLocation(CustomFlyingRocket pEntity) {
        return spriteTexture;
    }
}

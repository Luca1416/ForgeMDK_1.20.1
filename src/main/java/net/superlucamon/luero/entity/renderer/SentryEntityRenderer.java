package net.superlucamon.luero.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.superlucamon.luero.entity.types.nonhostile.SentryEntity;
import net.superlucamon.luero.entity.models.CustomSentryModel;
import org.jetbrains.annotations.Nullable;

public class SentryEntityRenderer extends LivingEntityRenderer<SentryEntity, CustomSentryModel<SentryEntity>> {
    private static final ResourceLocation SKIN = new ResourceLocation("heromod", "textures/entity/ironman_skin.png");
    private static final ResourceLocation SKINv2 = new ResourceLocation("heromod", "textures/entity/ironman_skinv2.png");
    private static SentryEntity sentry;

    public SentryEntityRenderer(EntityRendererProvider.Context context) {
            super(context, new CustomSentryModel(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
            //this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected boolean shouldShowName(SentryEntity pEntity) {
        return false;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(SentryEntity pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
        return RenderType.entityTranslucent(getTextureLocation(pLivingEntity));
    }

    @Override
    public void render(SentryEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        sentry = pEntity;
        //RenderType renderType = RenderType.tran(SKINv2);
       // this.model.setupAnim(pEntity, pEntityYaw, pEntity.swingTime, pEntity.tickCount + pPartialTicks, pEntity.yHeadRotO, pEntity.yHeadRot);
       // this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(renderType), pPackedLight, LivingEntityRenderer.getOverlayCoords(pEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SentryEntity entity) {
        return sentry.readyForTransform ? SKINv2 : SKIN;
    }
}
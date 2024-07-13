package net.superlucamon.luero.test;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static net.superlucamon.luero.item.custom.TestItem.renderDataMap;

@OnlyIn(Dist.CLIENT)
public class TargetedEntitySpriteRenderer extends EntityRenderer<Entity>  {
    private static TargetedEntitySpriteRenderer instance;
    private static final ResourceLocation spriteTexture = new ResourceLocation("heromod", "textures/item/target_marker_arrow.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(spriteTexture);


    public TargetedEntitySpriteRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public static TargetedEntitySpriteRenderer getInstance(EntityRendererProvider.Context context) {
        if (instance == null) {
            instance = new TargetedEntitySpriteRenderer(context);
        }
        return instance;
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource, int packedLight) {
        //super.render(entity, entityYaw, partialTicks, matrixStack, bufferSource, packedLight);

        renderSpriteAboveEntity(entity, entityYaw, partialTicks, matrixStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity pEntity) {
        return null;
    }

    public void renderSpriteAboveEntity(Entity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource, int packedLight) {
        matrixStack.pushPose();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
      //  matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
       // matrixStack.translate(entity.getX(), entity.getEyeHeight() * 2.5f, entity.getZ());
        PoseStack.Pose posestack$pose = matrixStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        if (renderDataMap != null) {
            for (int i = 1; i < renderDataMap.get(entity) + 1; i++) {
                float lOffset = i * 0.42f;
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RENDER_TYPE);
                vertex(entity, vertexconsumer, matrix4f, matrix3f, packedLight, -0.5F, -0.5F + lOffset, 0, 1);
                vertex(entity, vertexconsumer, matrix4f, matrix3f, packedLight, 0.5F, -0.5F + lOffset, 1, 1);
                vertex(entity, vertexconsumer, matrix4f, matrix3f, packedLight, 0.5F, 0.5F + lOffset, 1, 0);
                vertex(entity, vertexconsumer, matrix4f, matrix3f, packedLight, -0.5F, 0.5F + lOffset, 0, 0);
            }
        }

        /*
        VertexConsumer vertexconsumer2 = bufferSource.getBuffer(RENDER_TYPE);
        vertex(entity, vertexconsumer2, matrix4f, matrix3f, packedLight, 0.0F, 0, 0, 1);
        vertex(entity, vertexconsumer2, matrix4f, matrix3f, packedLight, 1.0F, 0, 1, 1);
        vertex(entity, vertexconsumer2, matrix4f, matrix3f, packedLight, 1.0F, 1, 1, 0);
        vertex(entity, vertexconsumer2, matrix4f, matrix3f, packedLight, 0.0F, 1, 0, 0);

         */
        matrixStack.popPose();
    }

    private static void vertex(Entity entity, VertexConsumer pConsumer, Matrix4f pPose, Matrix3f pNormal, int pLightmapUV, float pX, float pY, int pU, int pV) {
        pConsumer.vertex(pPose, pX, pY + (2.5f * entity.getEyeHeight()), 0.0F)
                 .color(255, 255, 255, 255).uv((float) pU, (float) pV)
                 .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pLightmapUV).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }
}



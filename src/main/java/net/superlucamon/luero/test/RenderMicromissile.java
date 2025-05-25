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
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.entity.projectile.MicromissileEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = "heromod", value = Dist.CLIENT)
public class RenderMicromissile extends EntityRenderer<MicromissileEntity> {
    private static final ResourceLocation spriteTexture = new ResourceLocation("heromod", "textures/entity/micromissile.png");
    public RenderMicromissile(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(MicromissileEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        // mostly lifted from ArrowRenderer
        matrixStackIn.pushPose();

        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(45.0F));

        matrixStackIn.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStackIn.translate(-4.0D, 0.0D, 0.0D);

        VertexConsumer builder = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
        Matrix4f matrix4f = matrixStackIn.last().pose();
        Matrix3f matrix3f = matrixStackIn.last().normal();
        vertex(matrix4f, matrix3f, builder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn);
        vertex(matrix4f, matrix3f, builder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn);

        for (int j = 0; j < 4; ++j) {
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            vertex(matrix4f, matrix3f, builder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLightIn);
            vertex(matrix4f, matrix3f, builder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLightIn);
        }

        matrixStackIn.popPose();
    }

    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, float x, float y, float z, float u, float v, float nx, float ny, float nz, int lightmap) {
        builder.vertex(matrix4f, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(lightmap)
                .normal(matrix3f, nx, nz, ny)
                .endVertex();
    }


    @Override
    public ResourceLocation getTextureLocation(MicromissileEntity pEntity) {
        return spriteTexture;
    }
}

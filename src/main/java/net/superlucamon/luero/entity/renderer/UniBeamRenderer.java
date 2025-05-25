// Clean UniBeam render implementation based on beacon beam style
package net.superlucamon.luero.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.superlucamon.luero.entity.projectile.UniBeamEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class UniBeamRenderer<T extends Entity> extends EntityRenderer<T> {
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("heromod", "textures/entity/uni_beam.png");

    public UniBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!(entity instanceof UniBeamEntity beam)) {
            return; // just to be safe
        }
        poseStack.pushPose();

        // Get direction vector and apply rotation
        double dx = entity.getLookAngle().x;
        double dy = entity.getLookAngle().y;
        double dz = entity.getLookAngle().z;

        float acosY = (float) Math.acos(dy);
        float atanXZ = (float) Math.atan2(dz, dx);

        poseStack.translate(0.0F, 0.0F, 0.0F); // beam origin height adjustment
        poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(1.5707964f - atanXZ)));
        poseStack.mulPose(Axis.XP.rotationDegrees((float) Math.toDegrees(acosY)));
        float intensity = 0.8f+ (float)Math.sin((entity.tickCount + partialTick) * 1f) * 0.2f;
        float maxBeamHeight = 15;
        float progress = beam.getBeamProgress(); // per-entity
        float eased = (float) (1 - Math.cos(progress * Math.PI * 0.5));
        float animatedHeight = (eased * maxBeamHeight);

        float[] beamColor = {1.0f, 1.0f, 1.0f};
        this.renderBeaconBeam(
                poseStack,
                buffer,
                BEAM_TEXTURE,
                partialTick,
                1F,
                entity.level().getGameTime(),
                0,
                animatedHeight,
                beamColor,
                0.2f,
                0.18f,
                intensity
        );
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(T pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    public static void renderBeaconBeam(PoseStack pPoseStack, MultiBufferSource pBufferSource, ResourceLocation pBeamLocation, float pPartialTick, float pTextureScale, long pGameTime, int pYOffset, float pHeight, float[] pColors, float pBeamRadius, float pGlowRadius, float pAlpha) {
        float i = pYOffset + pHeight;
        pPoseStack.pushPose();
        pPoseStack.translate(0.0D, 0.0D, 0.0D);
        float f = (float)Math.floorMod(pGameTime, 40) + pPartialTick;
        float f1 = pHeight < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float)Mth.floor(f1 * 0.1F));
        float f3 = pColors[0];
        float f4 = pColors[1];
        float f5 = pColors[2];
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(30F));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(f * 5 * 2.25F - 45.0F));
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = -pBeamRadius;
        float f10 = 0.0F;
        float f11 = 0.0F;
        float f12 = -pBeamRadius;
        float f13 = 0.0F;
        float f14 = 1.0F;
        float f15 = -1.0F + f2;
        float f16 = (float)pHeight * pTextureScale * (0.5F / pBeamRadius) + f15;
        renderPart(pPoseStack, pBufferSource.getBuffer(RenderType.beaconBeam(pBeamLocation, true)), f3, f4, f5, pAlpha, pYOffset, i, 0.0F, pBeamRadius, pBeamRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
        pPoseStack.popPose();
        f6 = -pGlowRadius;
        float f7 = -pGlowRadius;
        f8 = -pGlowRadius;
        f9 = -pGlowRadius;
        f13 = 0.0F;
        f14 = 1.0F;
        f15 = -1.0F + f2;
        f16 = (float)pHeight * pTextureScale + f15;
        float multiplier = 1.1f;
        renderPart(pPoseStack, pBufferSource.getBuffer(RenderType.beaconBeam(pBeamLocation, true)), f3, f4, f5, 0.225F * pAlpha, pYOffset, i, f6, f7, pGlowRadius, f8, f9, pGlowRadius, pGlowRadius, pGlowRadius, 0.0F, 1.0F, f16, f15);
        renderPart(pPoseStack, pBufferSource.getBuffer(RenderType.beaconBeam(pBeamLocation, true)), f3, f4, f5, 0.125F * pAlpha, pYOffset, i, f6 * multiplier, f7 * multiplier, pGlowRadius * multiplier, f8 * multiplier, f9 * multiplier, pGlowRadius * multiplier, pGlowRadius * multiplier, pGlowRadius * multiplier, 0.0F, 1.0F, f16, f15);
        pPoseStack.popPose();
    }

    private static void renderPart(PoseStack pPoseStack, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha, int pMinY, float pMaxY, float pX0, float pZ0, float pX1, float pZ1, float pX2, float pZ2, float pX3, float pZ3, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        PoseStack.Pose posestack$pose = pPoseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX0, pZ0, pX1, pZ1, pMinU, pMaxU, pMinV, pMaxV);
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX3, pZ3, pX2, pZ2, pMinU, pMaxU, pMinV, pMaxV);
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX1, pZ1, pX3, pZ3, pMinU, pMaxU, pMinV, pMaxV);
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX2, pZ2, pX0, pZ0, pMinU, pMaxU, pMinV, pMaxV);
    }

    private static void renderQuad(Matrix4f pPose, Matrix3f pNormal, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha, int pMinY, float pMaxY, float pMinX, float pMinZ, float pMaxX, float pMaxZ, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMaxY, pMinX, pMinZ, pMaxU, pMinV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMinX, pMinZ, pMaxU, pMaxV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxX, pMaxZ, pMinU, pMaxV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMaxY, pMaxX, pMaxZ, pMinU, pMinV);
    }

    private static void addVertex(Matrix4f pPose, Matrix3f pNormal, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha, float pY, float pX, float pZ, float pU, float pV) {
        pConsumer.vertex(pPose, pX, (float)pY, pZ).color(pRed, pGreen, pBlue, pAlpha).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }



    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return BEAM_TEXTURE;
    }

}

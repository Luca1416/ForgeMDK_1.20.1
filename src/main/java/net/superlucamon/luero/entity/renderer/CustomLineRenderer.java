package net.superlucamon.luero.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.superlucamon.luero.test.MyRenderTypes;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CustomLineRenderer<T extends Entity> extends EntityRenderer {


    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");
    private static final ResourceLocation spriteTexture = new ResourceLocation("heromod", "textures/item/testbeam.png");
    public static final ResourceLocation CRYSTAL_BEAM_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png");
    private static final RenderType BEAM = RenderType.entitySmoothCutout(spriteTexture);
    private float test = 0;

    public CustomLineRenderer(EntityRendererProvider.Context pContext) {super(pContext);}

    @Override
    public void render(Entity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            System.out.println("No player");
            return;
        }

        // Start and end points of the line
        //Vec3 start = mc.player.getPosition(partialTicks);

        // Calculate the direction vector based on player's rotation
        // float pitch = mc.player.getXRot();
        // float yaw = mc.player.getYRot();

        // float pitchRad = (float) Math.toRadians(pitch);
        // float yawRad = (float) Math.toRadians(yaw);

        //double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        //double dy = -Math.sin(pitchRad);
        //double dz = Math.cos(yawRad) * Math.cos(pitchRad);

        //   Vec3 end = start.add(dx * 10, dy * 10, dz * 10);
        // Vec3 endtest = entity.position();
        // Get the player's look direction
        Vec3 lookVec = mc.player.getLookAngle();
        boolean isFirstPerson = mc.options.getCameraType().isFirstPerson();

        // Calculate yaw and pitch from look direction
        float yaw = (float) -(Mth.atan2(lookVec.z, lookVec.x) * (180f / Math.PI)) - 90;
        float pitch = (float) ((Mth.atan2(lookVec.y, Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z)) * (180 / Math.PI))-90);

        matrixStack.pushPose();
        if (isFirstPerson){
            matrixStack.mulPose(Axis.YP.rotationDegrees(yaw));
            matrixStack.mulPose(Axis.XP.rotationDegrees(pitch));
            if (pitch >= -134){
                // entity.setPos(mc.player.position().x, mc.player.position().y + 1.25f, mc.player.position().z);
            }
            if (pitch <= -134){
              /*  Vec3 start = mc.player.position();
                float pitcht = 0.0F;
                float yawt = 0.0F;

                // Alternatively, you can set yaw to the player's body rotation (getYHeadRot() or getYRot())
                yawt = mc.player.getYHeadRot();
                entity.setDeltaMovement(mc.player.getDeltaMovement());

                float pitchRad = (float) Math.toRadians(pitcht);
                float yawRad = (float) Math.toRadians(yawt);

                double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
                double dy = -Math.sin(pitchRad);
                double dz = Math.cos(yawRad) * Math.cos(pitchRad);

                // Create the direction vector
                Vec3 direction = new Vec3(dx, dy + -4.025, dz);

                // Extend the direction vector to get the end position for raycasting
                double reachDistance = -0.4D; // Example reach distance
                Vec3 end = start.add(direction.scale(reachDistance));

                entity.setPos(end);

               */
            }

        }
        else {
            matrixStack.mulPose(Axis.YP.rotationDegrees(yaw));
            matrixStack.mulPose(Axis.XP.rotationDegrees(pitch));
        }
        /*matrixStack.translate(-mc.getEntityRenderDispatcher().camera.getPosition().x,
                -mc.getEntityRenderDispatcher().camera.getPosition().y,
                -mc.getEntityRenderDispatcher().camera.getPosition().z);

         */

        long i = entity.level().getGameTime();
        float colors[] = new float[] {1,1,255};
        //colors[0] = 1;
        //colors[1] = 1;
        //colors[2] = 255;
        //  List<BeaconBlockEntity.BeaconBeamSection> list = BeaconBlockEntity;
        int j = 0;

        for(int k = 0; k < 1000; ++k) {
            //  BeaconBlockEntity.BeaconBeamSection beaconblockentity$beaconbeamsection = list.get(k);
            renderBeaconBeam(mc.player, matrixStack, buffer, partialTicks, i, j, k != 1024? 100 : 1000, colors);
            j += 100;
        }





        //matrixStack.translate(-entity.position().x,-entity.position().y,-entity.position().z);

        //   drawLine(matrixStack, buffer, start, endtest, 0, 0, 1, 1.0f); // Blue line

      /* float f = getY(entity, partialTicks);
        float f3 = (float)mc.player.getX() + 0.5F;
        float f4 = (float)mc.player.getY() - 0.5F;
        float f5 = (float)mc.player.getZ() + 0.5F;
        float f6 = (float)((double)f3 - entity.getX()-0.7f);
        float f7 = (float)((double)f4 - entity.getY() -0.2f);
        float f8 = (float)((double)f5 - entity.getZ()-0.7f);
        matrixStack.translate(f6, f7, f8);
        renderCrystalBeams(-f6, -f7 + f, -f8, partialTicks, entity.tickCount, matrixStack, buffer, packedLight);
       */
        matrixStack.popPose();
    }


    public static float getY(Entity pEndCrystal, float pPartialTick) {
        float f = (float)pEndCrystal.tickCount + pPartialTick;
        float f1 = Mth.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }

    public static void renderCrystalBeams(float pX, float pY, float pZ, float pPartialTick, int pTickCount, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f = Mth.sqrt(pX * pX + pZ * pZ);
        float f1 = Mth.sqrt(pX * pX + pY * pY + pZ * pZ);
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 2.0F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotation((float)(-Math.atan2((double)pZ, (double)pX)) - ((float)Math.PI / 2F)));
        pPoseStack.mulPose(Axis.XP.rotation((float)(-Math.atan2((double)f, (double)pY)) - ((float)Math.PI / 2F)));
        VertexConsumer vertexconsumer = pBuffer.getBuffer(BEAM);
        float f2 = 0.0F - ((float)pTickCount + pPartialTick) * 0.01F;
        float f3 = Mth.sqrt(pX * pX + pY * pY + pZ * pZ) / 32.0F - ((float)pTickCount + pPartialTick) * 0.01F;
        int i = 8;
        float f4 = 0.0F;
        float f5 = 0.75F;
        float f6 = 0.0F;
        PoseStack.Pose posestack$pose = pPoseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

        for(int j = 1; j <= 8; ++j) {
            float f7 = Mth.sin((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f8 = Mth.cos((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f9 = (float)j / 8.0F;
            vertexconsumer.vertex(matrix4f, f4 * 0.2F, f5 * 0.2F, 0.0F).color(0, 0, 0, 255).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f4, f5, f1).color(255, 255, 255, 255).uv(f6, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7, f8, f1).color(255, 255, 255, 255).uv(f9, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7 * 0.2F, f8 * 0.2F, 0.0F).color(0, 0, 0, 255).uv(f9, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }

        pPoseStack.popPose();
    }



    private static void renderBeaconBeam(Entity pEntity, PoseStack pPoseStack, MultiBufferSource pBufferSource, float pPartialTick, long pGameTime, int pYOffset, int pHeight, float pColors[]) {
        renderBeaconBeam(pEntity, pPoseStack, pBufferSource, BEAM_LOCATION, pPartialTick, 0.2F, pGameTime, pYOffset, pHeight, pColors, 0.05F, 0.052F);
    }
    private static void renderPart(PoseStack pPoseStack, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha, int pMinY, int pMaxY, float pX0, float pZ0, float pX1, float pZ1, float pX2, float pZ2, float pX3, float pZ3, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        PoseStack.Pose posestack$pose = pPoseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX0, pZ0, pX1, pZ1, pMinU, pMaxU, pMinV, pMaxV);
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX3, pZ3, pX2, pZ2, pMinU, pMaxU, pMinV, pMaxV);
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX1, pZ1, pX3, pZ3, pMinU, pMaxU, pMinV, pMaxV);
        renderQuad(matrix4f, matrix3f, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxY, pX2, pZ2, pX0, pZ0, pMinU, pMaxU, pMinV, pMaxV);
    }


    private static void renderQuad(Matrix4f pPose, Matrix3f pNormal, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha, int pMinY, int pMaxY, float pMinX, float pMinZ, float pMaxX, float pMaxZ, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMaxY, pMinX, pMinZ, pMaxU, pMinV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMinX, pMinZ, pMaxU, pMaxV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxX, pMaxZ, pMinU, pMaxV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMaxY, pMaxX, pMaxZ, pMinU, pMinV);
    }
    private static void addVertex(Matrix4f pPose, Matrix3f pNormal, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha, int pY, float pX, float pZ, float pU, float pV) {
        pConsumer.vertex(pPose, pX, (float)pY, pZ).color(pRed, pGreen, pBlue, pAlpha).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(pNormal, 0.0F, 0.0F, 0.0F).endVertex();
    }

    public static void renderBeaconBeam(Entity pEntity, PoseStack pPoseStack, MultiBufferSource pBufferSource, ResourceLocation pBeamLocation, float pPartialTick, float pTextureScale, long pGameTime, int pYOffset, int pHeight, float[] pColors, float pBeamRadius, float pGlowRadius) {
        int i = pYOffset + pHeight;
        pPoseStack.pushPose();
        // pPoseStack.translate(0.0D, 0.0D, 0.0D);
        float f = (float)Math.floorMod(pGameTime, 40) + pPartialTick;
        float f1 = pHeight < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float)Mth.floor(f1 * 0.1F));
        float f3 = pColors[0] * 1000;
        float f4 = pColors[1];
        float f5 = pColors[2]* 1000;


        pPoseStack.pushPose();
      /*  pPoseStack.translate(-Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().x,
                -Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().y,
                -Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().z);

       */


        //pPoseStack.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
        //pPoseStack.mulPose(Axis.XP.rotationDegrees(f * 2.25F - 45.0F));
        //  pPoseStack.mulPose(Axis.XP.rotation((float) pEntity.getXRot()));
      /*  double playerX = pEntity.getX();
        double playerY = pEntity.getY() + pEntity.getEyeHeight();
        double playerZ = pEntity.getZ();

        double entityX = pEntity.getX();
        double entityY = pEntity.getY() + pEntity.getEyeHeight();
        double entityZ = pEntity.getZ();

        // Calculate the direction vector
        double dx = playerX - entityX;
        double dy = playerY - entityY;
        double dz = playerZ - entityZ;

        // Calculate the yaw and pitch to face the player
        float yaw = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;
        float pitch = (float) (-(Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * (180F / Math.PI)));


       */
        //  pPoseStack.translate(0D, 0D, 0.0D); // Adjust as necessary
        // pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        //   pPoseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        //  pPoseStack.translate(0.0D, -1.5D, 0.0D); // Adjust as necessary
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
        renderPart(pPoseStack, pBufferSource.getBuffer(MyRenderTypes.brightSolid(pBeamLocation)), f3, f4, f5, 1, pYOffset, i, 0.0F, pBeamRadius, pBeamRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1, f16, f15);
        pPoseStack.popPose();
        f6 = -pGlowRadius;
        float f7 = -pGlowRadius;
        f8 = -pGlowRadius;
        f9 = -pGlowRadius;
        f13 = 0.0F;
        f14 = 1.0F;
        f15 = -1.0F + f2;
        f16 = (float)pHeight * pTextureScale + f15;
        renderPart(pPoseStack, pBufferSource.getBuffer(RenderType.lightning()), f3, f4, f5,1.25f, pYOffset, i, f6, f7, pGlowRadius, f8, f9, pGlowRadius, pGlowRadius, pGlowRadius, 0, 1F, f16, f15);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
        pPoseStack.popPose();
    }
    private void drawLine(PoseStack matrixStack, MultiBufferSource buffer, Vec3 start, Vec3 end, float r, float g, float b, float alpha) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(spriteTexture));
        PoseStack.Pose pose = matrixStack.last();

        vertexConsumer.vertex(pose.pose(), (float) start.x, (float) start.y, (float) start.z)
                .color(0.0F, 0.0F, 1.0F, 1.0F)
                .uv(0.0F, 0.0F)
                .overlayCoords(OverlayTexture.RED_OVERLAY_V)
                .uv2(15728880)
                .normal(pose.normal(), 0.0F, 1.0F, 0.0F)
                .endVertex();

        vertexConsumer.vertex(pose.pose(), (float) end.x, (float) end.y, (float) end.z)
                .color(0.0F, 0.0F, 1.0F, 1.0F)
                .uv(1.0F, 1.0F)
                .overlayCoords(OverlayTexture.RED_OVERLAY_V)
                .uv2(15728880)
                .normal(pose.normal(), 0.0F, 1.0F, 0.0F)
                .endVertex();
    }


    @Override
    public ResourceLocation getTextureLocation(Entity pEntity) {
        return null;
    }
}


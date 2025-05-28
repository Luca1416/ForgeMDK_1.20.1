package net.superlucamon.luero.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.heros.ironman.otherstuff.BeamTracking;

@Mod.EventBusSubscriber(modid = "heromod", value = Dist.CLIENT)
public class ClientBeamRenderer {
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("heromod", "textures/entity/uni_beam.png");

    public static void render(PoseStack matrixStack, MultiBufferSource buffer, float partialTicks, double beamDistance) {
        Minecraft mc = Minecraft.getInstance();

        for (Player player : mc.level.players()) {
            if (!BeamTracking.isFiring(player.getUUID())) continue;

            Vec3 eyePos = player.getEyePosition(partialTicks);
            Vec3 viewVec = player.getViewVector(partialTicks);
            Vec3 to = eyePos.add(viewVec.scale(beamDistance));

            // Raycast for collision
            HitResult hit = player.pick(beamDistance, partialTicks, true);
            double distance = beamDistance;
            if (hit.getType() != HitResult.Type.MISS) {
                distance = hit.getLocation().distanceTo(eyePos);
            }

            Vec3 direction = to.subtract(eyePos).normalize();
            float acosY = (float) Math.acos(direction.y);
            float atan2XZ = (float) Math.atan2(direction.z, direction.x);

            matrixStack.pushPose();
            matrixStack.translate(0.0, player.getEyeHeight(Pose.STANDING) - 2.0, 0.0);
            matrixStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(1.5707964f - atan2XZ)));
            matrixStack.mulPose(Axis.XP.rotationDegrees((float) Math.toDegrees(acosY)));

            float flicker = 0.8f + (float)Math.sin((player.tickCount + partialTicks) * 0.15f) * 0.2f;
            float[] beamColor = new float[] { 1.0f, 1.0f, 1.0f };

            UniBeamRenderer.renderBeaconBeam(
                    matrixStack,
                    buffer,
                    BEAM_TEXTURE,
                    partialTicks,
                    1.0f,
                    player.level().getGameTime(),
                    0,
                    (int) distance,
                    beamColor,
                    0.2f,
                    0.18f,
                    flicker
            );

            matrixStack.popPose();
        }
    }


    private static boolean shouldRenderBeam(LocalPlayer player) {
        return true;
    }
}

package net.superlucamon.luero.entity.renderer;// Step 1: BeamRenderManager (client-side only)

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "heromod", value = Dist.CLIENT)
public class ClientBeamRenderManager {
    private static final Map<UUID, BeamRenderData> ACTIVE_BEAMS = new HashMap<>();
    private static final ResourceLocation TEXTURE = new ResourceLocation("heromod", "textures/entity/uni_beam.png");

    public static void startBeam(UUID playerId) {
        ACTIVE_BEAMS.put(playerId, new BeamRenderData());
    }

    public static void stopBeam(UUID playerId) {
        ACTIVE_BEAMS.remove(playerId);
    }

    public static void renderAllBeams(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        for (UUID id : ACTIVE_BEAMS.keySet()) {
            Player player = mc.level.getPlayerByUUID(id);
            if (player == null) continue;

            BeamRenderData data = ACTIVE_BEAMS.get(id);
            data.updateProgress();
            float progress = data.getProgress();
            float alpha = 0.6f + (float)Math.sin((player.tickCount + partialTick) * 0.2f) * 0.4f;

            poseStack.pushPose();

            Vec3 viewVec = mc.gameRenderer.getMainCamera().getPosition();
            Vec3 eyePos = player.getEyePosition(partialTick);
            poseStack.translate(eyePos.x - viewVec.x, eyePos.y - viewVec.y - 0.5f, eyePos.z - viewVec.z);

            float yaw = (float)(Math.toDegrees(Math.atan2(player.getLookAngle().z, player.getLookAngle().x))) - 90;
            float pitch = (float)(-Math.toDegrees(Math.asin(player.getLookAngle().y)));
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.YP.rotationDegrees(pitch));


            int beamHeight = (int)(progress * 15);
            float[] beamColor = {1.0f, 1.0f, 1.0f};

            BeaconRenderer.renderBeaconBeam(
                    poseStack, buffer, TEXTURE,
                    partialTick, 1.0f, player.level().getGameTime(),
                    0, beamHeight, beamColor,
                    0.2f, 0.25f
            );
            poseStack.popPose();
        }
    }

    private static class BeamRenderData {
        private float progress = 0;
        public void updateProgress() {
            if (progress < 1.0f) {
                progress += 0.025f;
            }
        }
        public float getProgress() {
            return progress;
        }
    }
}

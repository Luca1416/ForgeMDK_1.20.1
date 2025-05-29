package net.superlucamon.luero.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "heromod", value = Dist.CLIENT)
public class ClientBeamRenderer {
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("heromod", "textures/entity/uni_beam.png");

    public static void render(PoseStack matrixStack, MultiBufferSource buffer, float partialTicks, double beamDistance) {
        for (Map.Entry<UUID, ClientBeamData.BeamInfo> entry : ClientBeamData.getAll().entrySet()) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(entry.getKey());
            if (player == null) continue;

            float safeTicks = Math.min(partialTicks + 0.05f, 1.0f);
            Vec3 origin = entry.getValue().getInterpolatedOrigin(safeTicks);
            Vec3 direction = entry.getValue().getInterpolatedDirection(safeTicks);
            Vec3 to = origin.add(direction.scale(beamDistance));

            float acosY = (float) Math.acos(direction.y);
            float atan2XZ = (float) Math.atan2(direction.z, direction.x);

            matrixStack.pushPose();
            matrixStack.translate(
                    origin.x - Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().x,
                    origin.y - Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().y,
                    origin.z - Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().z
            );
            matrixStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(1.5707964f - atan2XZ)));
            matrixStack.mulPose(Axis.XP.rotationDegrees((float) Math.toDegrees(acosY)));

            float flicker = 0.8f + (float)Math.sin((player.tickCount + partialTicks) * 0.15f) * 0.2f;

            UniBeamRenderer.renderBeaconBeam(
                    matrixStack,
                    buffer,
                    BEAM_TEXTURE,
                    partialTicks,
                    1.0f,
                    player.level().getGameTime(),
                    0,
                    (float)beamDistance,
                    new float[] { 1, 1, 1 },
                    0.2f,
                    0.18f,
                    flicker
            );

            matrixStack.popPose();
        }
    }

    private static boolean shouldRenderBeam(Player player) {
        return true;
    }
}

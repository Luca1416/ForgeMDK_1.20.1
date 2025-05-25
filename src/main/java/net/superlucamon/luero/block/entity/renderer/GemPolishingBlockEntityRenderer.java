package net.superlucamon.luero.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.superlucamon.luero.block.entity.GemPolishingStationBlockEntity;
import org.joml.Matrix4f;

public class GemPolishingBlockEntityRenderer implements BlockEntityRenderer<GemPolishingStationBlockEntity> {
    public GemPolishingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(GemPolishingStationBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderStack();
        // ✅ Render output item (your existing code)
        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.75f, 0.5f);
        pPoseStack.scale(0.35f, 0.35f, 0.35f);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(270));
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();

        if (pBlockEntity.getProgress() > 0) {
            float progress = (float) pBlockEntity.getProgress() / pBlockEntity.getMaxProgress();
            //System.out.println("[DEBUG] Progress: " + progress);

            float width = 1.0f * progress; // full block wide
            float height = 0.1f;

            pPoseStack.pushPose();
            pPoseStack.translate(0.0f, 1.5f, 0.0f); // render clearly above block

            VertexConsumer vc = pBuffer.getBuffer(net.minecraft.client.renderer.RenderType.gui());
            Matrix4f matrix = pPoseStack.last().pose();

            vc.vertex(matrix, 0f, 0f, 0f).color(0f, 1f, 0f, 1f).endVertex();                   // bottom left
            vc.vertex(matrix, 0f, height, 0f).color(0f, 1f, 0f, 1f).endVertex();               // top left
            vc.vertex(matrix, width, height, 0f).color(0f, 1f, 0f, 1f).endVertex();            // top right
            vc.vertex(matrix, width, 0f, 0f).color(0f, 1f, 0f, 1f).endVertex();                // bottom right

            pPoseStack.popPose();
        }
    }


    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
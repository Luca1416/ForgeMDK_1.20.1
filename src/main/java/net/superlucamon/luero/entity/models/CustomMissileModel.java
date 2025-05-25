package net.superlucamon.luero.entity.models;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;


public class CustomMissileModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart main;
    private final ModelPart uno;
    private final ModelPart dos;
    private final ModelPart dres;
    private final ModelPart quadro;
    private final ModelPart root;

    public CustomMissileModel(ModelPart root) {
        this.root = root;
        this.main = root.getChild("main");
        this.uno = main.getChild("uno");
        this.dos = main.getChild("dos");
        this.dres = main.getChild("dres");
        this.quadro = main.getChild("quadro");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4778F, -0.5F, 0.0222F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-0.4778F, -0.5F, -0.2778F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(0, 0).addBox(-0.4778F, -0.5F, -0.6778F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(7, 7).addBox(-0.4778F, -0.5F, -0.9778F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.15F))
                .texOffs(0, 0).mirror().addBox(-0.6778F, 0.0F, 4.5222F, 1.0F, 0.0F, -1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(7, 2).mirror().addBox(-0.4778F, -0.5F, -1.2778F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false)
                .texOffs(7, 2).addBox(-0.4778F, -0.5F, -1.4778F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(7, 2).addBox(-0.4778F, -0.5F, -1.6778F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(7, 2).addBox(-0.4778F, -0.5F, -1.8778F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.4778F, 23.5F, -4.0222F, 0.0F, 0.0F, -3.1416F));

        PartDefinition uno = main.addOrReplaceChild("uno", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5777F, 0.0F, 3.7722F, 3.0543F, 0.0F, 1.5708F));

        PartDefinition cube_r1 = uno.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 3).addBox(-0.6999F, -1.5491F, 1.7714F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7F, 0.0F, -2.75F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r2 = uno.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(3, 1).addBox(-0.7F, -0.1084F, -3.4743F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7F, 0.0F, -2.75F, 3.0107F, 0.0F, 0.0F));

        PartDefinition dos = main.addOrReplaceChild("dos", CubeListBuilder.create(), PartPose.offsetAndRotation(0.9223F, 0.0F, 3.7722F, 3.0543F, 0.0F, 1.5708F));

        PartDefinition cube_r3 = dos.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 3).addBox(-0.7999F, -1.3643F, 1.848F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, -2.75F, -0.3927F, 0.0F, 3.1416F));

        PartDefinition cube_r4 = dos.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(3, 1).addBox(-0.8F, -0.3067F, -3.5004F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, -2.75F, 3.0107F, 0.0F, -3.1416F));

        PartDefinition dres = main.addOrReplaceChild("dres", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0223F, -0.6F, 3.7722F, 0.0F, 3.0543F, 0.0F));

        PartDefinition cube_r5 = dres.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 3).addBox(-0.0999F, -2.1034F, 1.5418F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 0.6F, -2.75F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r6 = dres.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(3, 1).addBox(-0.1F, 0.4864F, -3.396F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 0.6F, -2.75F, 3.0107F, 0.0F, 0.0F));

        PartDefinition quadro  = main.addOrReplaceChild("quadro", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0223F, 0.6F, 3.7722F, -3.1416F, -0.0873F, 0.0F));

        PartDefinition cube_r7 = quadro.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 3).addBox(-0.0999F, -2.1034F, 1.5418F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 0.6F, -2.75F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r8 = quadro.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(3, 1).addBox(-0.1F, 0.4864F, -3.396F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 0.6F, -2.75F, 3.0107F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
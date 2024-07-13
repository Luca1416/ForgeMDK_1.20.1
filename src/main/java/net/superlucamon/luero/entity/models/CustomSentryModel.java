package net.superlucamon.luero.entity.models;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.PathfinderMob;
import net.superlucamon.luero.entity.types.nonhostile.SentryEntity;

public class CustomSentryModel<S extends PathfinderMob> extends PlayerModel<SentryEntity> {
    public CustomSentryModel(ModelPart root) {
        super(root, false);
    }

    @Override
    public void setupAnim(SentryEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
       // if (!pEntity.isAttacking){
            super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
       // }
        if (pEntity.isAttacking){
            this.rightLeg.xRot = 0;
            this.leftLeg.xRot = 0;
            this.rightSleeve.xRot = 4.7f;
            this.rightArm.xRot = 4.7f;
           // AnimationUtils.animateZombieArms(rightArm, leftArm, false, attackTime, pAgeInTicks);
        }
    }
}